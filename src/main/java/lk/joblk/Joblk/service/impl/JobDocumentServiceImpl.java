package lk.joblk.Joblk.service.impl;

import lk.joblk.Joblk.dto.JobDocumentDto;
import lk.joblk.Joblk.entity.JobDetails;
import lk.joblk.Joblk.entity.JobDocument;
import lk.joblk.Joblk.entity.User;
import lk.joblk.Joblk.repo.JobDetailsRepo;
import lk.joblk.Joblk.repo.JobDocumentRepo;
import lk.joblk.Joblk.repo.UserRepo;
import lk.joblk.Joblk.service.JobDocumentService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class JobDocumentServiceImpl implements JobDocumentService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JobDocumentRepo jobDocumentRepo;

    @Autowired
    JobDetailsRepo jobDetailsRepo;


    @Override
    public JobDocumentDto saveJobDocument(JobDocumentDto jobDocumentDto, int jobId) {
        jobDetailsRepo.findById (jobId)
                .orElseThrow (() -> new RuntimeException ("Job post not found with ID: " + jobId));

        JobDocument jobDocument = new JobDocument ();
        jobDocument.setUsername (jobDocumentDto.getUsername ());
        jobDocument.setQualifications (jobDocumentDto.getQualifications ());
        jobDocument.setNumber (jobDocumentDto.getNumber ());
        jobDocument.setGender (jobDocumentDto.getGender ());
        jobDocument.setImagePath (jobDocumentDto.getImagePath ());
        jobDocument.setCvPath (jobDocumentDto.getCvPath ());
        jobDocument.setApplyDate (jobDocumentDto.getApplyDate ());
        jobDocument.setUserEmail (jobDocumentDto.getUserEmail ());
        jobDocument.setAge (jobDocumentDto.getAge ());
        jobDocument.setAddress (jobDocumentDto.getAddress ());

        jobDocument.setJobId (jobId);

        JobDocument saved = jobDocumentRepo.save (jobDocument);

        return new JobDocumentDto (
                saved.getId (),
                saved.getUsername (),
                saved.getQualifications (),
                saved.getNumber (),
                saved.getGender (),
                saved.getImagePath (),
                saved.getCvPath (),
                saved.getApplyDate (),
                saved.getUserEmail (),
                saved.getAge (),
                saved.getAddress ()
        );
    }


    public JobDocument createJobDocument(JobDocumentDto request) {
        JobDocument doc = new JobDocument();
        doc.setUsername(request.getUsername());
        doc.setQualifications(request.getQualifications());
        doc.setAge(request.getAge());
        doc.setGender(request.getGender());
        doc.setImagePath(request.getImagePath());
        doc.setCvPath(request.getCvPath());
        doc.setApplyDate(request.getApplyDate());
        doc.setUserEmail(request.getUserEmail());
        doc.setNumber(request.getNumber());
        doc.setAddress(request.getAddress());
        doc.setJobId(request.getJobId());

        // Get JobDetails
        JobDetails jobDetails = jobDetailsRepo.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Get User from JobDetails
        User user = jobDetails.getUser();
        if (user == null) {
            throw new RuntimeException("User not found for this job");
        }

        // Copy data from JobDetails
        doc.setJobTitle(jobDetails.getJobTitle());

        doc.setUserid (user.getUserId ());

        // Link entities


        return jobDocumentRepo.save(doc);
    }


    public JobDocument createJobDocuments(JobDocument jobDocument) {
        // Fetch the JobDetails by jobId
        JobDetails jobDetails = jobDetailsRepo.findById(jobDocument.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found for id: " + jobDocument.getJobId()));

        // Set the jobTitle and userId from JobDetails
        jobDocument.setJobTitle(jobDetails.getJobTitle());
        jobDocument.setUserid(String.valueOf(jobDetails.getUser().getUserId ()));  // Assuming user has an 'id' field

        // Save the JobDocument
        return jobDocumentRepo.save(jobDocument);
    }


    //cv document upload for Jobs
    @Override
    public String saveFile(MultipartFile file, int id) {
        try {
            String fileName = file.getOriginalFilename ();
            Path uploadPath = Paths.get ("upload", fileName);

            Files.createDirectories (uploadPath.getParent ());

            Files.write (uploadPath, file.getBytes (), StandardOpenOption.CREATE);

            String fileUrl = "http://localhost:8080/upload/" + fileName;

            JobDocument  jobDocument = jobDocumentRepo.findById (id).orElseThrow (() -> new RuntimeException (" Job not found with ID: " + id));

            jobDocument.setCvPath (fileUrl);
            jobDocumentRepo.save (jobDocument);

            return "00";

        } catch (IOException e) {
            throw new RuntimeException ("File upload failed: " + e.getMessage ());
        }
    }



    public byte[] getCvDocument(int id) {
        JobDocument jobDocument = jobDocumentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job document not found with id: " + id));

        String cvUrl = jobDocument.getCvPath();
        // Extract the file name from the URL
        String fileName = cvUrl.substring(cvUrl.lastIndexOf("/") + 1);

        // Define the file path where the CV is stored
        Path cvPath = Paths.get("upload/", fileName);

        if (!Files.exists(cvPath)) {
            throw new RuntimeException("Cv document not found for Jobs : " + id);
        }

        try {
            return Files.readAllBytes(cvPath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading CV document for Jobs: " + id, e);
        }
    }

    public String getFileExtension(int id) {
        JobDocument jobDocument = jobDocumentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job document not found with id: " + id));

        String cvUrl = jobDocument.getCvPath();
        if (cvUrl == null || !cvUrl.contains(".")) {
            return null;
        }
        return cvUrl.substring(cvUrl.lastIndexOf(".") + 1);
    }


    public MediaType getMediaTypeForFileExtension(String extension) {
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




    @SneakyThrows
    @Override
    public int imageUpload(MultipartFile file, int id) {

        String fileName = file.getOriginalFilename ();

        Path uploadPath = Paths.get ("upload/", fileName);

        Files.createDirectories (uploadPath.getParent ());

        Files.write (uploadPath, file.getBytes ());

        String fileUrl = "http://localhost:8080/upload/" + fileName;

        JobDocument jobDocument = jobDocumentRepo.findById (id).orElseThrow (() -> new RuntimeException ("Job Document  not found with ID: " + id));

        jobDocument.setImagePath (fileUrl);

        JobDocument saveImgJobDocument = jobDocumentRepo.save (jobDocument);

        return 1;
    }

    @Override
    public byte[] getImage(int id) throws IOException {
        JobDocument jobDocument = jobDocumentRepo.findById (id).orElseThrow (() -> new RuntimeException ("Job Document not found with id: " + id));

        String imgUrl = jobDocument.getImagePath ();
        System.out.println ("image url :" + imgUrl);

        String fileName = imgUrl.substring (imgUrl.lastIndexOf ("/") + 1);
        System.out.println ("file name :" + fileName);

        Path imgPath = Paths.get ("upload/", fileName);
        System.out.println ("image path :" + imgPath);

        if (!Files.exists (imgPath)) {
            throw new FileNotFoundException ("Image not found for Job Document id: " + id);
        }

        return Files.readAllBytes (imgPath);
    }

    @Override
    public String deleteImage(int Id) {
        if (jobDocumentRepo.existsById (Id)) {
            Optional<JobDocument> byId = jobDocumentRepo.findById (Id);
            byId.get ().setImagePath (null);
            jobDocumentRepo.save (byId.get ());
        }
        return "Image deleted successfully";
    }

    @Override
    public String updateImage(int id, MultipartFile file) throws IOException {

        if (jobDocumentRepo.existsById (id)) {
            JobDocument jobDocument = jobDocumentRepo.findById (id).get ();

            String fileName = file.getOriginalFilename ();

            Path uploadPath = Paths.get ("upload/", fileName);

            Files.createDirectories (uploadPath.getParent ());

            Files.write (uploadPath, file.getBytes ());

            String fileUrl = "http://localhost:8080/upload/" + fileName;

            jobDocument.setImagePath (fileUrl);

            JobDocument saveDocumentImage = jobDocumentRepo.save (jobDocument);

            return "Image updated successfully";
        }

        return "Image not updated";
    }

    public List<JobDocumentDto> getAllJobDouments() {
        List<JobDocument> jobDocumentsAll = jobDocumentRepo.findAllOrderedByApplyDate (); // Use the new query
        List<JobDocumentDto> jobDocumentDtoList = new ArrayList<> ();

        for (JobDocument jobDocument : jobDocumentsAll) {

            JobDocumentDto jobDocumentDto = new JobDocumentDto(
                    jobDocument.getJobId(),
                    jobDocument.getUsername (),
                    jobDocument.getQualifications (),
                    jobDocument.getAge (),
                    jobDocument.getGender (),
                    jobDocument.getImagePath (),
                    jobDocument.getCvPath (),
                    jobDocument.getApplyDate (),
                    jobDocument.getId (),
                    jobDocument.getNumber () ,
                    jobDocument.getUserEmail (),
                    jobDocument.getAddress (),
                    jobDocument.getUserid () ,
                    jobDocument.getJobTitle ()
                    
            );

            jobDocumentDtoList.add(jobDocumentDto);
        }
        return jobDocumentDtoList;
    }

    @Override
    public String deleteDoucment(int id) {
       if (jobDocumentRepo. existsById (id)){
           jobDocumentRepo.deleteById (id);
           return "Job document deleted successfully";
       }else {
           return "Job document not deleted";
       }
    }




}


