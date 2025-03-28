package lk.joblk.Joblk.service;


import lk.joblk.Joblk.dto.LoginDto;
import lk.joblk.Joblk.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {
    void register(UserDto userDto);

    String login(LoginDto loginDto);

    UserDto getUser(String username);

    List<UserDto> getAllUsers();

    String updateUser(UserDto userDto);

    String deleteUser(String userName);

    int imageUploadProfile(MultipartFile file, String id);

    byte[] getImageProfile(String id) throws IOException;

    String deleteImageProfile(String useId);

    String updateImageProfile(String userId, MultipartFile file) throws IOException;


    int imageUploadCover(MultipartFile file, String id);

    byte[] getImageCover(String id) throws IOException;

    String deleteImageCover(String userId);

    String updateImageCover(String userId, MultipartFile file) throws IOException;

    String deleteUserId(String id);

    String saveFile(MultipartFile file, String userId) throws IOException;

    byte[] getCvDocument(String userId);
}
