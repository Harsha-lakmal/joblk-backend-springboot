package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.dto.CourseDto;
import lk.joblk.Joblk.dto.ResponseDto;
import lk.joblk.Joblk.entity.Course;
import lk.joblk.Joblk.repo.CourseRepo;
import lk.joblk.Joblk.service.CourseService;
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
@RequestMapping("api/v1/course")
public class CourseController {
    @Autowired
    ResponseDto responseDto;
    @Autowired
    CourseService courseService;

    @Autowired
    CourseRepo courseRepo;

@PostMapping("/addCourse/{userId}")
public ResponseEntity<CourseDto> courseSave(@PathVariable String userId, @RequestBody CourseDto courseDto) {
    CourseDto savedCourse = courseService.courseSave(courseDto, userId);
    return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
}


    @PutMapping("/updateCourse")
    public ResponseEntity<ResponseDto> updateCourse(@RequestBody CourseDto courseDto) {
        try {
            String res = courseService.updateCourse (courseDto);
            if (res.equals ("05")) {
                responseDto.setMessage ("Success to update course..");
                responseDto.setCode (VarList.RSP_SUCCESS);
                responseDto.setContent (courseDto);
                return new ResponseEntity<> (responseDto, HttpStatus.ACCEPTED);
            } else if (res.equals ("01")) {
                responseDto.setMessage ("DUPLICATED job course ...");
                responseDto.setCode (VarList.RSP_DUPLICATED);
                responseDto.setContent (courseDto);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            } else {

                responseDto.setMessage ("Error  course not update ");
                responseDto.setCode (VarList.RSP_ERROR);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            responseDto.setMessage ("Error  course not update ");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteCourse/{courseId}")
    public ResponseEntity<ResponseDto> deleteCourse(@PathVariable int courseId) {

        try {
            String res = courseService.deleteCourse (courseId);
            if (res.equals ("00")) {
                responseDto.setMessage ("Success to delete course..");
                responseDto.setCode (VarList.RSP_SUCCESS);
                responseDto.setContent (courseId);
                return new ResponseEntity<> (responseDto, HttpStatus.ACCEPTED);
            } else {
                responseDto.setMessage ("Error  user not course ");
                responseDto.setCode (VarList.RSP_ERROR);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            responseDto.setMessage ("Error  not delete  course ");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);

        }


    }

    @GetMapping("/searchCourse/{courseId}")
    public ResponseEntity<ResponseDto> searchJob(@PathVariable int courseId) {
        try {
            CourseDto courseDto = courseService.getCourse (courseId);
            if (courseDto != null) {
                responseDto.setMessage ("Success to search course..");
                responseDto.setCode (VarList.RSP_SUCCESS);
                responseDto.setContent (courseDto);
                return new ResponseEntity<> (responseDto, HttpStatus.OK);

            } else {
                responseDto.setMessage ("Error  user not course ");
                responseDto.setCode (VarList.RSP_ERROR);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            responseDto.setMessage ("Error  user not course ");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);

        }
    }


    @GetMapping("/getAllCourse")
    public ResponseEntity getAllCourses() {
        try {
            List<CourseDto> allCourse = courseService.getAllCourse ();
            responseDto.setMessage ("Success to get course..");
            responseDto.setCode (VarList.RSP_SUCCESS);
            responseDto.setContent (allCourse);
            return new ResponseEntity<> (responseDto, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            responseDto.setMessage ("Error get course");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
        }
    }


    //image upload for course image

    @PostMapping("/upload/{courseId}")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file, @PathVariable int courseId) throws IOException {
        int i = courseService.imageUpload (file, courseId);

        if (i == 1) {
            return new ResponseEntity<String> ("Upload Success !", HttpStatus.CREATED);
        }
        return new ResponseEntity<> ("Upload Failed", HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/get/image/{courseId}")
    public ResponseEntity<byte[]> getImage(@PathVariable int courseId) throws IOException {

        Optional<Course> courseOpt = courseRepo.findById (courseId);
        if (!courseOpt.isPresent ()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }

        Course course = courseOpt.get ();
        String imgUrl = course.getImgPath ();

        String fileExtension = getFileExtension (imgUrl);
        if (fileExtension == null || fileExtension.isEmpty ()) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }

        byte[] image;
        try {
            image = courseService.getImage (courseId);
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

    @DeleteMapping("/delete/image/{courseId}")
    public String imageDelete(@PathVariable int courseId) {
        String s = courseService.deleteImage (courseId);
        return s;
    }

    @PutMapping("/update/image/{courseId}")
    public String updateImage(@PathVariable int courseId, @RequestParam("file") MultipartFile file) throws IOException {
        String s = courseService.updateImage (courseId, file);
        return s;

    }


}
