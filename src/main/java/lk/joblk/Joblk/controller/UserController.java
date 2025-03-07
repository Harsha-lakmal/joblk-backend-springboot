package lk.joblk.Joblk.controller;


import lk.joblk.Joblk.dto.LoginDto;
import lk.joblk.Joblk.dto.ResponseDto;
import lk.joblk.Joblk.dto.UserDto;
import lk.joblk.Joblk.entity.User;
import lk.joblk.Joblk.repo.UserRepo;
import lk.joblk.Joblk.service.UserService;
import lk.joblk.Joblk.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ResponseDto responseDto;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        try {
            userService.register (userDto);
            return new ResponseEntity<> (userDto.getUsername () + " user registered..!!", HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<> (exception.getMessage (), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        try {
            return new ResponseEntity<> (userService.login (loginDto), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<> (exception.getMessage (), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getUser/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        try {
            UserDto userDto = userService.getUser (username);
            return new ResponseEntity<> (userDto, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (Exception exception) {
            return new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            return new ResponseEntity<> (userService.getAllUsers (), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/updateUser")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody UserDto userDto) {
        ResponseDto responseDto = new ResponseDto ();

        try {
            String res = userService.updateUser (userDto);

            if (VarList.RSP_SUCCESS.equals (res)) {
                responseDto.setMessage ("Successfully updated user.");
                responseDto.setCode (VarList.RSP_SUCCESS);
                responseDto.setContent (userDto);
                return new ResponseEntity<> (responseDto, HttpStatus.OK);
            } else if (VarList.RSP_DUPLICATED.equals (res)) {
                responseDto.setMessage ("Duplicate entry. User update failed.");
                responseDto.setCode (VarList.RSP_DUPLICATED);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.CONFLICT);
            } else {
                responseDto.setMessage ("Error: User update failed.");
                responseDto.setCode (VarList.RSP_ERROR);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            responseDto.setMessage ("Exception occurred: " + e.getMessage ());
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteUser/{userName}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable String userName) {

        try {
            String res = userService.deleteUser (userName);
            if (res.equals ("00")) {
                responseDto.setMessage ("Success to delete User..");
                responseDto.setCode (VarList.RSP_SUCCESS);
                responseDto.setContent (userName);
                return new ResponseEntity<> (responseDto, HttpStatus.ACCEPTED);
            } else {
                responseDto.setMessage ("Error  user not User ");
                responseDto.setCode (VarList.RSP_ERROR);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            responseDto.setMessage ("Error  not delete  User ");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);

        }


    }


    //image upload part of profile image

    @PostMapping("/uploadProfile/{userId}")
    public ResponseEntity<String> imageUploadProfile(@RequestParam("file") MultipartFile file, @PathVariable String userId) throws IOException {
        int i = userService.imageUploadProfile (file, userId);

        if (i == 1) {
            return new ResponseEntity<String> ("Upload Success !", HttpStatus.CREATED);
        }
        return new ResponseEntity<> ("Upload Failed", HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/get/imageProfile/{userId}")
    public ResponseEntity<byte[]> getImageProfile(@PathVariable String userId) throws IOException {

        Optional<User> userOpt = userRepo.findById (userId);
        if (!userOpt.isPresent ()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get ();
        String imgUrl = user.getImgPathProfile ();

        String fileExtension = getFileExtension (imgUrl);
        if (fileExtension == null || fileExtension.isEmpty ()) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }

        byte[] image;
        try {
            image = userService.getImageProfile (userId);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MediaType mediaType = getMediaTypeForFileExtension (fileExtension);

        HttpHeaders headers = new HttpHeaders ();
        headers.setContentType (mediaType);

        return new ResponseEntity<> (image, headers, HttpStatus.OK);
    }

    private String getFileExtension(String url) {
        if (url == null || !url.contains (".")) {
            return null;
        }
        return url.substring (url.lastIndexOf (".") + 1);
    }

    private MediaType getMediaTypeForFileExtension(String extension) {
            switch (extension.toLowerCase ()) {
                case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "webp":
                return MediaType.valueOf ("image/webp");
            case "bmp":
                return MediaType.valueOf ("image/bmp");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @DeleteMapping("/delete/imageProfile/{userId}")
    public String imageDeleteProfile(@PathVariable String userId) throws IOException {
        String s = userService.deleteImageProfile (userId);
        return s;
    }

    @PutMapping("/update/imageProfile/{userId}")
    public String updateImageProfile(@PathVariable String userId, @RequestParam("file") MultipartFile file) throws IOException {
        String s = userService.updateImageProfile (userId, file);
        return s;

    }


    //image upload part  of cover image

    @PostMapping("/uploadCover/{userId}")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file, @PathVariable String userId) throws IOException {
        int i = userService.imageUploadCover (file, userId);

        if (i == 1) {
            return new ResponseEntity<String> ("Upload Success !", HttpStatus.CREATED);
        }
        return new ResponseEntity<> ("Upload Failed", HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/get/imageCover/{userId}")
    public ResponseEntity<byte[]> getImageCover(@PathVariable String userId) throws IOException {

        Optional<User> userOpt = userRepo.findById (userId);
        if (!userOpt.isPresent ()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get ();
        String imgUrl = user.getImgPathCover ();

        String fileExtension = getFileExtension (imgUrl);
        if (fileExtension == null || fileExtension.isEmpty ()) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }

        byte[] image;
        try {
            image = userService.getImageCover (userId);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MediaType mediaType = getMediaTypeForFileExtension (fileExtension);

        HttpHeaders headers = new HttpHeaders ();
        headers.setContentType (mediaType);

        return new ResponseEntity<> (image, headers, HttpStatus.OK);
    }


    @DeleteMapping("/delete/imageCover/{userId}")
    public String imageDeleteCover(@PathVariable String userId) {
        String s = userService.deleteImageCover (userId);
        return s;
    }

    @PutMapping("/update/imageCover/{userId}")
    public String updateImageCover(@PathVariable String userId, @RequestParam("file") MultipartFile file) throws IOException {
        String s = userService.updateImageCover (userId, file);
        return s;

    }


}
