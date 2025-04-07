package lk.joblk.Joblk.service;

import lk.joblk.Joblk.dto.JobDocumentDto;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface JobDocumentService {
    JobDocumentDto saveJobDocument(JobDocumentDto jobDocumentDto, int jobId);

    String saveFile(MultipartFile file, int id);

    byte[] getCvDocument(int id);

    String getFileExtension(int id);

    MediaType getMediaTypeForFileExtension(String fileExtension);


    int imageUpload(MultipartFile file, int id);

    byte[] getImage(int id) throws IOException;

    String deleteImage(int id);

    String updateImage(int id, MultipartFile file) throws IOException;


    List<JobDocumentDto> getAllJobDouments();

    String deleteDoucment(int id);
}
