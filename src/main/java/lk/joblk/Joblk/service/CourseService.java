package lk.joblk.Joblk.service;


import lk.joblk.Joblk.dto.CourseDto;
import lk.joblk.Joblk.dto.JobDetailsDto;
import lk.joblk.Joblk.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {


    String addCourse(CourseDto courseDto, String userId);

    String updateCourse(CourseDto courseDto);

    String deleteCourse(int id);

    CourseDto getCourse(int id);

    List<CourseDto> getAllCourse();

    int imageUpload(MultipartFile file, int id);

    byte[] getImage(int id) throws IOException;

    String deleteImage(int courseId);

    String updateImage(int courseId, MultipartFile file) throws IOException;

    CourseDto courseSave(CourseDto courseDto, String userId);

    List<CourseDto> getAllCourseDetails();


    List<CourseDto> getCourseUserId(String userId);
}

