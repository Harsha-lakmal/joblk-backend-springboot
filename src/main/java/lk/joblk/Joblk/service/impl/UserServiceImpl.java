package lk.joblk.Joblk.service.impl;

import lk.joblk.Joblk.dto.LoginDto;
import lk.joblk.Joblk.dto.UserDto;
import lk.joblk.Joblk.entity.User;
import lk.joblk.Joblk.repo.UserRepo;
import lk.joblk.Joblk.service.UserService;
import lk.joblk.Joblk.service.auth.JwtService;
import lk.joblk.Joblk.utils.Converter;
import lk.joblk.Joblk.utils.UserRoles;
import lk.joblk.Joblk.utils.VarList;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private Converter converter;
    @Autowired
    @Lazy
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ModelMapper modelMapper;


    public void register(UserDto userDto) {
        String encodedPassword = Base64.getEncoder ().encodeToString (userDto.getPassword ().getBytes ());
        if (userRepo.existsByUsername (userDto.getUsername ())) {
            throw new RuntimeException (userDto.getUsername () + " already exists");
        }

        User user = new User ();
        user.setUserId (userDto.getId ());
        user.setUsername (userDto.getUsername ());
//        user.setPassword (encodedPassword);
        user.setPassword (userDto.getPassword ());
        user.setEmail (userDto.getEmail ());
        user.setImgPathCover (userDto.getImgPathCover ());
        user.setImgPathProfile (userDto.getImgPathProfile ());
        user.setRole (UserRoles.valueOf (userDto.getRole ()));
        User saveUser = userRepo.save (user);

    }


    @Override
    public String login(LoginDto loginDto) {
        AuthenticationManager authenticationManager = new ProviderManager (authenticationProvider);
        Authentication authentication = authenticationManager.authenticate (new UsernamePasswordAuthenticationToken (loginDto.username (), loginDto.password ()));
        if (authentication.isAuthenticated ()) {
            return jwtService.generateToken (loadUserByUsername (loginDto.username ()));
        }
        throw new RuntimeException ("Bad Request..!!");
    }

    @Override
    public UserDto getUser(String username) {
        return userRepo.findByUsername (username).map (user -> modelMapper.map (user, UserDto.class)).orElseThrow (() -> new RuntimeException ("User not found with username: " + username));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepo.findByUsername (username);
        if (byUsername.isPresent ()) {
            User user = byUsername.get ();
            return org.springframework.security.core.userdetails.User.builder ().username (user.getUsername ()).password (user.getPassword ()).roles (String.valueOf (user.getRole ())).build ();
        } else throw new UsernameNotFoundException (username);

    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.findAll ();
        return users.stream ().map (user -> modelMapper.map (user, UserDto.class)).collect (Collectors.toList ());
    }


    @Override
    public String updateUser(UserDto userDto) {
        Optional<User> optionalUser = userRepo.findById (userDto.getId ());

        if (optionalUser.isPresent ()) {
            User updateUserDetails = optionalUser.get ();

            updateUserDetails.setUsername (userDto.getUsername ());
            updateUserDetails.setPassword (userDto.getPassword ());
            updateUserDetails.setImgPathCover (userDto.getImgPathCover ());
            updateUserDetails.setImgPathProfile (userDto.getImgPathProfile ());
            updateUserDetails.setRole (UserRoles.valueOf (userDto.getRole ()));
            updateUserDetails.setEmail (userDto.getEmail ());

            userRepo.save (updateUserDetails);
            return VarList.RSP_SUCCESS;
        } else {
            return VarList.RSP_DUPLICATED;
        }
    }

    @Override
    public String deleteUser(String userName) {
        if (userRepo.existsByUsername (userName)) {
            Optional<User> byUsername = userRepo.findByUsername (userName);
            if (byUsername.isPresent ()) {
                userRepo.delete (byUsername.get ());
                return VarList.RSP_SUCCESS;
            }

        }
        return VarList.RSP_NO_DATA_FOUND;
    }


    @SneakyThrows
    @Override
    public int imageUploadProfile(MultipartFile file, String userId) {

        String fileName = file.getOriginalFilename ();

        Path uploadPath = Paths.get ("upload/", fileName);

        Files.createDirectories (uploadPath.getParent ());

        Files.write (uploadPath, file.getBytes ());

        String fileUrl = "http://localhost:8080/upload/" + fileName;

        User user = userRepo.findById (userId).orElseThrow (() -> new RuntimeException ("User  not found with ID: " + userId));

        user.setImgPathProfile (fileUrl);

        User saveUserProfileImg = userRepo.save (user);

        return 1;
    }

    @Override
    public byte[] getImageProfile(String userId) throws IOException {
        User user = userRepo.findById (userId).orElseThrow (() -> new RuntimeException ("user not found with id: " + userId));

        String imgUrl = user.getImgPathProfile ();
        System.out.println ("image url :" + imgUrl);

        String fileName = imgUrl.substring (imgUrl.lastIndexOf ("/") + 1);
        System.out.println ("file name :" + fileName);

        Path imgPath = Paths.get ("upload/", fileName);
        System.out.println ("image path :" + imgPath);

        if (!Files.exists (imgPath)) {
            throw new FileNotFoundException ("Image not found for Course id: " + user);
        }

        return Files.readAllBytes (imgPath);
    }

    @Override
    public String deleteImageProfile(String Id) {
        if (userRepo.existsById (Id)) {
            Optional<User> byId = userRepo.findById (Id);
            byId.get ().setImgPathProfile (null);
            userRepo.save (byId.get ());
        }
        return "Image deleted successfully";
    }

    @Override
    public String updateImageProfile(String id, MultipartFile file) throws IOException {

        if (userRepo.existsById (id)) {
            User user = userRepo.findById (id).get ();

            String fileName = file.getOriginalFilename ();

            Path uploadPath = Paths.get ("upload/", fileName);

            Files.createDirectories (uploadPath.getParent ());

            Files.write (uploadPath, file.getBytes ());

            String fileUrl = "http://localhost:8080/upload/" + fileName;

            user.setImgPathProfile (fileUrl);

            User save = userRepo.save (user);

            return "Image updated successfully";
        }

        return "Image not updated";
    }


    @SneakyThrows
    @Override
    public int imageUploadCover(MultipartFile file, String userId) {

        String fileName = file.getOriginalFilename ();

        Path uploadPath = Paths.get ("upload/", fileName);

        Files.createDirectories (uploadPath.getParent ());

        Files.write (uploadPath, file.getBytes ());

        String fileUrl = "http://localhost:8080/upload/" + fileName;

        User user = userRepo.findById (userId).orElseThrow (() -> new RuntimeException ("user  not found with ID: " + userId));

        user.setImgPathCover (fileUrl);

        User saveUserCoverImg = userRepo.save (user);

        return 1;
    }

    @Override
    public byte[] getImageCover(String userId) throws IOException {
        User user = userRepo.findById (userId).orElseThrow (() -> new RuntimeException ("Course not found with id: " + userId));

        String imgUrl = user.getImgPathCover ();
        System.out.println ("image url :" + imgUrl);

        String fileName = imgUrl.substring (imgUrl.lastIndexOf ("/") + 1);
        System.out.println ("file name :" + fileName);

        Path imgPath = Paths.get ("upload/", fileName);
        System.out.println ("image path :" + imgPath);

        if (!Files.exists (imgPath)) {
            throw new FileNotFoundException ("Image not found for Course id: " + userId);
        }

        return Files.readAllBytes (imgPath);
    }

    @Override
    public String deleteImageCover(String Id) {
        if (userRepo.existsById (Id)) {
            Optional<User> byId = userRepo.findById (Id);
            byId.get ().setImgPathCover (null);
            userRepo.save (byId.get ());
        }
        return "Image deleted successfully";
    }

    @Override
    public String updateImageCover(String id, MultipartFile file) throws IOException {

        if (userRepo.existsById (id)) {
            User user = userRepo.findById (id).get ();

            String fileName = file.getOriginalFilename ();

            Path uploadPath = Paths.get ("upload/", fileName);

            Files.createDirectories (uploadPath.getParent ());

            Files.write (uploadPath, file.getBytes ());

            String fileUrl = "http://localhost:8080/upload/" + fileName;

            user.setImgPathCover (fileUrl);

            User save = userRepo.save (user);

            return "Image updated successfully";
        }

        return "Image not updated";
    }

}




