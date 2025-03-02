package lk.joblk.Joblk.service.impl;


import lk.joblk.Joblk.dto.CourseDto;
import lk.joblk.Joblk.entity.Course;
import lk.joblk.Joblk.entity.User;
import lk.joblk.Joblk.repo.CourseRepo;
import lk.joblk.Joblk.repo.UserRepo;
import lk.joblk.Joblk.service.CourseService;
import lk.joblk.Joblk.utils.VarList;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private UserRepo userRepo;


    public String addCourse(CourseDto courseDto, String userId) {
        if (courseRepo.existsById (courseDto.getCourseId ())) {
            return VarList.RSP_DUPLICATED;
        }

        Course course = new Course ();
        course.setCourseId (courseDto.getCourseId ());
        course.setCourseStartDate (courseDto.getCourseStartDate ());
        course.setCourseContent (courseDto.getCourseContent ());
        course.setCourseDescription (courseDto.getCourseDescription ());
        course.setCourseQualification (courseDto.getCourseQualification ());
        course.setCourseTitle (courseDto.getCourseTitle ());
        course.setCourseLocation (courseDto.getCourseLocation ());
        course.setImgPath (courseDto.getImgPath ());

        User user = userRepo.findById (String.valueOf (userId)).orElseThrow (() -> new RuntimeException ("User not found"));
        course.setUser (user);

        courseRepo.save (course);
        return VarList.RSP_SUCCESS;
    }


    @Override
    public String updateCourse(CourseDto courseDto) {

        if (courseRepo.existsById (courseDto.getCourseId ())) {
            Course existingCourse = courseRepo.findById (courseDto.getCourseId ()).orElse (null);

            if (existingCourse != null) {
                existingCourse.setCourseTitle (courseDto.getCourseTitle ());
                existingCourse.setCourseDescription (courseDto.getCourseDescription ());
                existingCourse.setCourseLocation (courseDto.getCourseLocation ());
                existingCourse.setCourseQualification (courseDto.getCourseQualification ());
                existingCourse.setCourseContent (courseDto.getCourseContent ());
                existingCourse.setCourseStartDate (courseDto.getCourseStartDate ());

                User user = userRepo.findById (String.valueOf ((courseDto.getCourseId ()))).orElse (null);
                if (user != null) {
                    existingCourse.setUser (user);
                } else {
                    return VarList.RSP_ERROR;
                }

                courseRepo.save (existingCourse);
                return VarList.RSP_SUCCESS;
            }
        }
        return VarList.RSP_DUPLICATED;
    }


    @Override
    public String deleteCourse(int id) {
        if (courseRepo.existsById (id)) {
            courseRepo.deleteById (id);
            return VarList.RSP_SUCCESS;

        } else {
            return VarList.RSP_DUPLICATED;

        }
    }

    @Override
    public CourseDto getCourse(int id) {

        if (courseRepo.existsById (id)) {
            Course course = courseRepo.findById (id).orElse (null);
            return modelMapper.map (course, CourseDto.class);

        } else {
            return null;
        }
    }

    @Override
    public List<CourseDto> getAllCourse() {
        List<Course> getAllCourse = courseRepo.findAll ();
        return modelMapper.map (getAllCourse, new TypeToken<List<CourseDto>> () {
        }.getType ());
    }

    @SneakyThrows
    @Override
    public int imageUpload(MultipartFile file, int courseId) {

        String fileName = file.getOriginalFilename ();

        Path uploadPath = Paths.get ("upload/", fileName);

        Files.createDirectories (uploadPath.getParent ());

        Files.write (uploadPath, file.getBytes ());

        String fileUrl = "http://localhost:8080/upload/" + fileName;

        Course course = courseRepo.findById (courseId).orElseThrow (() -> new RuntimeException ("Course  not found with ID: " + courseId));

        course.setImgPath (fileUrl);

        Course saveCourseImg = courseRepo.save (course);

        return 1;
    }

    @Override
    public byte[] getImage(int courseId) throws IOException {
        Course course = courseRepo.findById (courseId).orElseThrow (() -> new RuntimeException ("Course not found with id: " + courseId));

        String imgUrl = course.getImgPath ();
        System.out.println ("image url :" + imgUrl);

        String fileName = imgUrl.substring (imgUrl.lastIndexOf ("/") + 1);
        System.out.println ("file name :" + fileName);

        Path imgPath = Paths.get ("upload/", fileName);
        System.out.println ("image path :" + imgPath);

        if (!Files.exists (imgPath)) {
            throw new FileNotFoundException ("Image not found for Course id: " + courseId);
        }

        return Files.readAllBytes (imgPath);
    }

    @Override
    public String deleteImage(int Id) {
        if (courseRepo.existsById (Id)) {
            Optional<Course> byId = courseRepo.findById (Id);
            byId.get ().setImgPath (null);
            courseRepo.save (byId.get ());
        }
        return "Image deleted successfully";
    }

    @Override
    public String updateImage(int id, MultipartFile file) throws IOException {

        if (courseRepo.existsById (id)) {
            Course course = courseRepo.findById (id).get ();

            String fileName = file.getOriginalFilename ();

            Path uploadPath = Paths.get ("upload/", fileName);

            Files.createDirectories (uploadPath.getParent ());

            Files.write (uploadPath, file.getBytes ());

            String fileUrl = "http://localhost:8080/upload/" + fileName;

            course.setImgPath (fileUrl);

            Course save = courseRepo.save (course);

            return "Image updated successfully";
        }

        return "Image not updated";
    }

}

