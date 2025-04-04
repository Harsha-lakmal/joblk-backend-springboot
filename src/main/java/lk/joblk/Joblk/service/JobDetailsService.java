package lk.joblk.Joblk.service;


import lk.joblk.Joblk.dto.JobDetailsDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface JobDetailsService {
    String addJob(JobDetailsDto jobDetailsDto, String userId);


    String deleteJob(int id);

    JobDetailsDto getJobDetails(int id);

    List<JobDetailsDto> getAllJobDetails();


    int imageUpload(MultipartFile file, int courseId);

    byte[] getImage(int jobId) throws IOException;

    String deleteImage(int jobId);

    String updateImage(int jobId, MultipartFile file) throws IOException;

    String updateJob(JobDetailsDto jobDetailsDto);

    JobDetailsDto jobsSave(JobDetailsDto jobDetailsDto, String userId);

    List<JobDetailsDto> getAllDetailsJobs();
}
