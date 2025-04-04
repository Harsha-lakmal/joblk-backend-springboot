package lk.joblk.Joblk.service.impl;

import lk.joblk.Joblk.dto.CourseDto;
import lk.joblk.Joblk.dto.JobDetailsDto;
import lk.joblk.Joblk.entity.Course;
import lk.joblk.Joblk.entity.JobDetails;
import lk.joblk.Joblk.entity.User;
import lk.joblk.Joblk.repo.JobDetailsRepo;
import lk.joblk.Joblk.repo.UserRepo;
import lk.joblk.Joblk.service.JobDetailsService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobDetailsServiceImpl implements JobDetailsService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private JobDetailsRepo jobDetailsRepo;

    @Autowired
    private UserRepo userRepo;


    @Override
    public String addJob(JobDetailsDto jobDetailsDto, String userId) {
        if (jobDetailsRepo.existsById (jobDetailsDto.getJobId ())) {
            return VarList.RSP_DUPLICATED;
        } else {
            JobDetails jobDetails = new JobDetails ();
            jobDetails.setJobId (jobDetailsDto.getJobId ());
            jobDetails.setJobTitle (jobDetailsDto.getJobTitle ());
            jobDetails.setJobDescription (jobDetailsDto.getJobDescription ());
            jobDetails.setJobClosingDate (jobDetailsDto.getJobClosingDate ());
            jobDetails.setQualifications (jobDetailsDto.getQualifications ());
            jobDetails.setImgPath (jobDetailsDto.getImgPath ());
            jobDetails.setDateUpload (jobDetailsDto.getDateUpload ());

            User user = userRepo.findById (String.valueOf (userId)).orElseThrow (() -> new RuntimeException ("User not found"));
            jobDetails.setUser (user);

            jobDetailsRepo.save (jobDetails);
            return VarList.RSP_SUCCESS;
        }
    }

    @Override
    public String deleteJob(int id) {
        if (jobDetailsRepo.existsById (id)) {
            jobDetailsRepo.deleteById (id);
            return VarList.RSP_SUCCESS;

        } else {
            return VarList.RSP_DUPLICATED;

        }
    }

    @Override
    public JobDetailsDto getJobDetails(int id) {
        if (jobDetailsRepo.existsById (id)) {
            JobDetails jobDetails = jobDetailsRepo.findById (id).orElse (null);
            return modelMapper.map (jobDetails, JobDetailsDto.class);

        } else {
            return null;
        }

    }

    @Override
    public List<JobDetailsDto> getAllJobDetails() {
        List<JobDetails> allJobs = jobDetailsRepo.findAll ();
        return modelMapper.map (allJobs, new TypeToken<ArrayList<JobDetailsDto>> () {
        }.getType ());
    }


    @SneakyThrows
    @Override
    public int imageUpload(MultipartFile file, int jobId) {

        String fileName = file.getOriginalFilename ();

        Path uploadPath = Paths.get ("upload/", fileName);

        Files.createDirectories (uploadPath.getParent ());

        Files.write (uploadPath, file.getBytes ());

        String fileUrl = "http://localhost:8080/upload/" + fileName;

        JobDetails jobDetails = jobDetailsRepo.findById (jobId).orElseThrow (() -> new RuntimeException ("Job  not found with ID: " + jobId));

        jobDetails.setImgPath (fileUrl);

        JobDetails saveCourseImg = jobDetailsRepo.save (jobDetails);

        return 1;
    }

    @Override
    public byte[] getImage(int jobId) throws IOException {
        JobDetails jobDetails = jobDetailsRepo.findById (jobId).orElseThrow (() -> new RuntimeException ("Job not found with id: " + jobId));

        String imgUrl = jobDetails.getImgPath ();
        System.out.println ("image url :" + imgUrl);

        String fileName = imgUrl.substring (imgUrl.lastIndexOf ("/") + 1);
        System.out.println ("file name :" + fileName);

        Path imgPath = Paths.get ("upload/", fileName);
        System.out.println ("image path :" + imgPath);

        if (!Files.exists (imgPath)) {
            throw new FileNotFoundException ("Image not found for Job id: " + jobId);
        }

        return Files.readAllBytes (imgPath);
    }

    @Override
    public String deleteImage(int Id) {
        if (jobDetailsRepo.existsById (Id)) {
            Optional<JobDetails> byId = jobDetailsRepo.findById (Id);
            byId.get ().setImgPath (null);
            jobDetailsRepo.save (byId.get ());
        }
        return "Image deleted successfully";
    }

    @Override
    public String updateImage(int id, MultipartFile file) throws IOException {

        if (jobDetailsRepo.existsById (id)) {
            JobDetails jobDetails = jobDetailsRepo.findById (id).get ();

            String fileName = file.getOriginalFilename ();

            Path uploadPath = Paths.get ("upload/", fileName);

            Files.createDirectories (uploadPath.getParent ());

            Files.write (uploadPath, file.getBytes ());

            String fileUrl = "http://localhost:8080/upload/" + fileName;

            jobDetails.setImgPath (fileUrl);

            JobDetails save = jobDetailsRepo.save (jobDetails);

            return "Image updated successfully";
        }

        return "Image not updated";
    }

    @Override
    public String updateJob(JobDetailsDto jobDetailsDto) {

        if (jobDetailsRepo.existsById (jobDetailsDto.getJobId ())) {
            JobDetails jobDetails = jobDetailsRepo.findById (jobDetailsDto.getJobId ()).orElse (null);

            if (jobDetails != null) {
                jobDetails.setJobTitle (jobDetailsDto.getJobTitle ());
                jobDetails.setJobDescription (jobDetailsDto.getJobDescription ());
                jobDetails.setImgPath (jobDetailsDto.getImgPath ());
                jobDetails.setQualifications (jobDetailsDto.getQualifications ());
                jobDetails.setJobClosingDate (jobDetailsDto.getJobClosingDate ());

                User user = userRepo.findById (String.valueOf ((jobDetailsDto.getJobId ()))).orElse (null);
                if (user != null) {
                    jobDetails.setUser (user);
                } else {
                    return VarList.RSP_ERROR;
                }

                jobDetailsRepo.save (jobDetails);
                return VarList.RSP_SUCCESS;
            }
        }
        return VarList.RSP_DUPLICATED;
    }

    @Override

    public JobDetailsDto jobsSave(JobDetailsDto jobDetailsDto, String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));


        JobDetails jobDetails = new JobDetails (
                jobDetailsDto.getJobId (),
                jobDetailsDto.getJobDescription (),
                jobDetailsDto.getJobTitle () ,
                jobDetailsDto.getQualifications () ,
                jobDetailsDto.getJobClosingDate () ,
                jobDetailsDto.getImgPath () ,
                jobDetailsDto.getDateUpload ()
        );

        jobDetails.setUser (user);
        JobDetails save = jobDetailsRepo.save (jobDetails);

        return  new  JobDetailsDto (save.getJobId (),save.getJobTitle (),save.getJobDescription (),save.getQualifications (),save.getJobClosingDate (),save.getImgPath (),save.getDateUpload ());

    }

//    @Override
//    public List<JobDetailsDto> getAllDetailsJobs() {
//            List<JobDetails> allDetailsJobs = jobDetailsRepo.findAll ();
//            List<JobDetailsDto> allDetailsJobsDto = new ArrayList<>();
//
//
//
//            for (JobDetails jobDetails : allDetailsJobs) {
//
//
//                String userId = jobDetails.getUser() != null ? jobDetails.getUser().getUserId() : "";
//
//
//                JobDetailsDto jobDetailsDto =  new  JobDetailsDto (
//
//                        jobDetails.getJobId () ,
//                        jobDetails.getJobDescription () ,
//                        jobDetails.getJobClosingDate () ,
//                        jobDetails.getDateUpload () ,
//                        jobDetails.getImgPath () ,
//                        jobDetails.getQualifications () ,
//                        jobDetails.getJobTitle () ,
//                        userId
//
//                );
//
//                allDetailsJobsDto.add (jobDetailsDto);
//
//
//            }
//        return allDetailsJobsDto ;
//    }



    public List<JobDetailsDto> getAllDetailsJobs() {
        List<JobDetails> allDetailsJobs = jobDetailsRepo.findAllOrderedByDateUpload(); // Use the new query
        List<JobDetailsDto> allDetailsJobsDto = new ArrayList<>();

        for (JobDetails jobDetails : allDetailsJobs) {
            String userId = jobDetails.getUser() != null ? jobDetails.getUser().getUserId() : "";

            JobDetailsDto jobDetailsDto = new JobDetailsDto(
                    jobDetails.getJobId(),
                    jobDetails.getJobDescription(),
                    jobDetails.getJobClosingDate(),
                    jobDetails.getDateUpload(),
                    jobDetails.getImgPath(),
                    jobDetails.getQualifications(),
                    jobDetails.getJobTitle(),
                    userId
            );

            allDetailsJobsDto.add(jobDetailsDto);
        }
        return allDetailsJobsDto;
    }
}
