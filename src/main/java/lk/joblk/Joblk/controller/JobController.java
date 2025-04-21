package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.dto.JobDetailsDto;
import lk.joblk.Joblk.dto.ResponseDto;
import lk.joblk.Joblk.entity.JobDetails;
import lk.joblk.Joblk.repo.JobDetailsRepo;
import lk.joblk.Joblk.service.JobDetailsService;
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
@RequestMapping("api/v1/job")
public class JobController {
    @Autowired
    ResponseDto responseDto;
    @Autowired
    JobDetailsService jobDetailsService;

    @Autowired
    JobDetailsRepo jobDetailsRepo;

    @PostMapping("/addJob/{userId}")
    public ResponseEntity<JobDetailsDto> jobsSave(@PathVariable String userId, @RequestBody JobDetailsDto jobDetailsDto) {
        JobDetailsDto savedJob = jobDetailsService.jobsSave (jobDetailsDto, userId);
        return new ResponseEntity<> (savedJob, HttpStatus.CREATED);
    }

    @PutMapping("/updateJob")
    public ResponseEntity<ResponseDto> updateJob(@RequestBody JobDetailsDto jobDetailsDto) {
        try {
            String res = jobDetailsService.updateJob (jobDetailsDto);
            if (res.equals ("05")) {
                responseDto.setMessage ("Success to update job..");
                responseDto.setCode (VarList.RSP_SUCCESS);
                responseDto.setContent (jobDetailsDto);
                return new ResponseEntity<> (responseDto, HttpStatus.ACCEPTED);
            } else if (res.equals ("01")) {
                responseDto.setMessage ("DUPLICATED job Saved ...");
                responseDto.setCode (VarList.RSP_DUPLICATED);
                responseDto.setContent (jobDetailsDto);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            } else {

                responseDto.setMessage ("Error  job not update ");
                responseDto.setCode (VarList.RSP_ERROR);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            responseDto.setMessage ("Error  job not update ");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/deleteJob/{jobId}")
    public ResponseEntity<ResponseDto> deleteJob(@PathVariable int jobId) {

        try {
            String res = jobDetailsService.deleteJob (jobId);
            if (res.equals ("00")) {
                responseDto.setMessage ("Success to delete job..");
                responseDto.setCode (VarList.RSP_SUCCESS);
                responseDto.setContent (jobId);
                return new ResponseEntity<> (responseDto, HttpStatus.ACCEPTED);
            } else {
                responseDto.setMessage ("Error  user not job ");
                responseDto.setCode (VarList.RSP_ERROR);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            responseDto.setMessage ("Error  not delete  job ");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);

        }


    }

    @GetMapping("/searchJob/{jobId}")
    public ResponseEntity<ResponseDto> searchJob(@PathVariable int jobId) {
        try {
            JobDetailsDto jobDetailsDto = jobDetailsService.getJobDetails (jobId);
            if (jobDetailsDto != null) {
                responseDto.setMessage ("Success to search job..");
                responseDto.setCode (VarList.RSP_SUCCESS);
                responseDto.setContent (jobDetailsDto);
                return new ResponseEntity<> (responseDto, HttpStatus.OK);

            } else {
                responseDto.setMessage ("Error  user not job ");
                responseDto.setCode (VarList.RSP_ERROR);
                responseDto.setContent (null);
                return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            responseDto.setMessage ("Error  user not job ");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/getAllJobs")
    public ResponseEntity getAllJobs() {
        try {
            List<JobDetailsDto> allUserslist = jobDetailsService.getAllJobDetails ();
            responseDto.setMessage ("Success to get job..");
            responseDto.setCode (VarList.RSP_SUCCESS);
            responseDto.setContent (allUserslist);
            return new ResponseEntity<> (responseDto, HttpStatus.ACCEPTED);


        } catch (Exception e) {
            responseDto.setMessage ("Error  get job ");
            responseDto.setCode (VarList.RSP_ERROR);
            responseDto.setContent (null);
            return new ResponseEntity<> (responseDto, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getAllJobsDetails")
    public ResponseEntity<List<JobDetailsDto>> getAllJobsDetails() {
        List<JobDetailsDto> allDetailsJobs = jobDetailsService.

                getAllDetailsJobs ();
        if (allDetailsJobs.isEmpty ()) {
            return ResponseEntity.noContent ().build ();
        }
        return ResponseEntity.ok (allDetailsJobs);
    }


    @GetMapping("/getJobsUsersId/{userId}")
    public ResponseEntity<List<JobDetailsDto>> getJobsUsersId(@PathVariable String userId) {
        List<JobDetailsDto> allJobsDetailsForUser = jobDetailsService.getJobsUserId (userId);
        if (allJobsDetailsForUser.isEmpty ()) {
            return ResponseEntity.noContent ().build ();
        }
        return ResponseEntity.ok (allJobsDetailsForUser);
    }


    //image upload part for job image

    @PostMapping("/upload/{jobId}")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file, @PathVariable int jobId) throws IOException {
        int i = jobDetailsService.imageUpload (file, jobId);

        if (i == 1) {
            return new ResponseEntity<String> ("Upload Success !", HttpStatus.CREATED);
        }
        return new ResponseEntity<> ("Upload Failed", HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/get/image/{jobId}")
    public ResponseEntity<byte[]> getImage(@PathVariable int jobId) throws IOException {

        Optional<JobDetails> jobDetailsOpt = jobDetailsRepo.findById (jobId);
        if (!jobDetailsOpt.isPresent ()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }

        JobDetails jobDetails = jobDetailsOpt.get ();
        String imgUrl = jobDetails.getImgPath ();

        String fileExtension = getFileExtension (imgUrl);
        if (fileExtension == null || fileExtension.isEmpty ()) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }

        byte[] image;
        try {
            image = jobDetailsService.getImage (jobId);
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


    @DeleteMapping("/delete/image/{jobId}")
    public String imageDelete(@PathVariable int jobId) {
        String s = jobDetailsService.deleteImage (jobId);
        return s;
    }

    @PutMapping("/update/image/{jobId}")
    public String updateImage(@PathVariable int jobId, @RequestParam("file") MultipartFile file) throws IOException {
        String s = jobDetailsService.updateImage (jobId, file);
        return s;
    }


}
