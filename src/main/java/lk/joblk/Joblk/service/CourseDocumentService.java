package lk.joblk.Joblk.service;

import lk.joblk.Joblk.dto.CourseDocumentDto;
import lk.joblk.Joblk.entity.CourseDocument;
import lk.joblk.Joblk.entity.JobDocument;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseDocumentService {
    CourseDocumentDto saveCourseDocument(CourseDocumentDto courseDocumentDto, int jobId);

    String saveFile(MultipartFile file, int id);

    byte[] getCvDocument(int id);

    String getFileExtension(int id);

    MediaType getMediaTypeForFileExtension(String fileExtension);


    int imageUpload(MultipartFile file, int id);

    byte[] getImage(int id) throws IOException;

    String deleteImage(int id);

    String updateImage(int id, MultipartFile file) throws IOException;


    List<CourseDocumentDto> getAllJobDouments();

    String deleteDoucment(int id);

    CourseDocumentDto createCourseDocument(CourseDocumentDto request);

    CourseDocument createCourseDocuments(CourseDocument courseDocument);
}
