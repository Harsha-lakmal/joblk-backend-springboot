package lk.joblk.Joblk.service.impl;

import lk.joblk.Joblk.dto.CourseDocumentDto;
import lk.joblk.Joblk.entity.Course;
import lk.joblk.Joblk.entity.CourseDocument;
import lk.joblk.Joblk.entity.User;
import lk.joblk.Joblk.repo.CourseDocumentRepo;
import lk.joblk.Joblk.repo.CourseRepo;
import lk.joblk.Joblk.service.CourseDocumentService;
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
public class CourseDocumentServiceImpl implements CourseDocumentService {

    @Autowired
    CourseDocumentRepo courseDocumentRepo;

    @Autowired
    CourseRepo courseRepo;


    @Override
    public CourseDocumentDto saveCourseDocument(CourseDocumentDto courseDocumentDto, int courseId) {
        courseDocumentRepo.findById (courseId).orElseThrow (() -> new RuntimeException ("Course  post not found with ID: " + courseId));

        CourseDocument courseDocument = new CourseDocument ();
        courseDocument.setUsername (courseDocumentDto.getUsername ());
        courseDocument.setQualifications (courseDocumentDto.getQualifications ());
        courseDocument.setNumber (courseDocumentDto.getNumber ());
        courseDocument.setGender (courseDocumentDto.getGender ());
        courseDocument.setImagePath (courseDocumentDto.getImagePath ());
        courseDocument.setCvPath (courseDocumentDto.getCvPath ());
        courseDocument.setApplyDate (courseDocumentDto.getApplyDate ());
        courseDocument.setUserEmail (courseDocumentDto.getUserEmail ());
        courseDocument.setAge (courseDocumentDto.getAge ());
        courseDocument.setAddress (courseDocumentDto.getAddress ());

        courseDocument.setCourseId (courseId);

        CourseDocument saved = courseDocumentRepo.save (courseDocument);

        return new CourseDocumentDto (saved.getId (), saved.getUsername (), saved.getQualifications (), saved.getNumber (), saved.getGender (), saved.getImagePath (), saved.getCvPath (), saved.getApplyDate (), saved.getUserEmail (), saved.getAge (), saved.getAddress ());
    }


    public CourseDocumentDto createCourseDocument(CourseDocumentDto request) {

        CourseDocument doc = new CourseDocument ();
        doc.setUsername (request.getUsername ());
        doc.setQualifications (request.getQualifications ());
        doc.setAge (request.getAge ());
        doc.setGender (request.getGender ());
        doc.setImagePath (request.getImagePath ());
        doc.setCvPath (request.getCvPath ());
        doc.setApplyDate (request.getApplyDate ());
        doc.setUserEmail (request.getUserEmail ());
        doc.setNumber (request.getNumber ());
        doc.setAddress (request.getAddress ());
        doc.setCourseId (request.getCourseId ());


        Course course = courseRepo.findById (request.getCourseId ()).orElseThrow (() -> new RuntimeException ("Course not found"));

        // Get User from JobDetails
        User user = course.getUser ();
        if (user == null) {
            throw new RuntimeException ("User not found for this job");
        }

        // Copy data from JobDetails
        doc.setCourseTitle (course.getCourseTitle ());

        doc.setUserid (user.getUserId ());

        // Link entities
        courseDocumentRepo.save (doc);

        return new CourseDocumentDto ();
    }


    public CourseDocument createCourseDocuments(CourseDocument courseDocument) {
        // Fetch the JobDetails by jobId
        Course course = courseRepo.findById (courseDocument.getCourseId ()).orElseThrow (() -> new RuntimeException ("Job not found for id: " + courseDocument.getCourseId ()));

        // Set the jobTitle and userId from JobDetails
        courseDocument.setCourseTitle (course.getCourseTitle ());
        courseDocument.setUserid (String.valueOf (course.getUser ().getUserId ()));  // Assuming user has an 'id' field

        // Save the JobDocument
        return courseDocumentRepo.save (courseDocument);
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

            CourseDocument courseDocument = courseDocumentRepo.findById (id).orElseThrow (() -> new RuntimeException (" Course not found with ID: " + id));

            courseDocument.setCvPath (fileUrl);
            courseDocumentRepo.save (courseDocument);

            return "00";

        } catch (IOException e) {
            throw new RuntimeException ("File upload failed: " + e.getMessage ());
        }
    }


    public byte[] getCvDocument(int id) {
        CourseDocument courseDocument = courseDocumentRepo.findById (id).orElseThrow (() -> new RuntimeException ("Course document not found with id: " + id));

        String cvUrl = courseDocument.getCvPath ();
        // Extract the file name from the URL
        String fileName = cvUrl.substring (cvUrl.lastIndexOf ("/") + 1);

        // Define the file path where the CV is stored
        Path cvPath = Paths.get ("upload/", fileName);

        if (!Files.exists (cvPath)) {
            throw new RuntimeException ("Cv document not found for Jobs : " + id);
        }

        try {
            return Files.readAllBytes (cvPath);
        } catch (IOException e) {
            throw new RuntimeException ("Error reading CV document for Jobs: " + id, e);
        }
    }

    public String getFileExtension(int id) {
        CourseDocument courseDocument = courseDocumentRepo.findById (id).orElseThrow (() -> new RuntimeException ("Course  document not found with id: " + id));

        String cvUrl = courseDocument.getCvPath ();
        if (cvUrl == null || !cvUrl.contains (".")) {
            return null;
        }
        return cvUrl.substring (cvUrl.lastIndexOf (".") + 1);
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

        CourseDocument courseDocument = courseDocumentRepo.findById (id).orElseThrow (() -> new RuntimeException ("course Document  not found with ID: " + id));

        courseDocument.setImagePath (fileUrl);

        CourseDocument saveImgJobDocument = courseDocumentRepo.save (courseDocument);

        return 1;
    }

    @Override
    public byte[] getImage(int id) throws IOException {
        CourseDocument courseDocument = courseDocumentRepo.findById (id).orElseThrow (() -> new RuntimeException ("Course Document not found with id: " + id));

        String imgUrl = courseDocument.getImagePath ();
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
        if (courseDocumentRepo.existsById (Id)) {
            Optional<CourseDocument> byId = courseDocumentRepo.findById (Id);
            byId.get ().setImagePath (null);
            courseDocumentRepo.save (byId.get ());
        }
        return "Image deleted successfully";
    }

    @Override
    public String updateImage(int id, MultipartFile file) throws IOException {

        if (courseDocumentRepo.existsById (id)) {
            CourseDocument courseDocument = courseDocumentRepo.findById (id).get ();

            String fileName = file.getOriginalFilename ();

            Path uploadPath = Paths.get ("upload/", fileName);

            Files.createDirectories (uploadPath.getParent ());

            Files.write (uploadPath, file.getBytes ());

            String fileUrl = "http://localhost:8080/upload/" + fileName;

            courseDocument.setImagePath (fileUrl);

            CourseDocument saveDocumentImage = courseDocumentRepo.save (courseDocument);

            return "Image updated successfully";
        }

        return "Image not updated";
    }

    public List<CourseDocumentDto> getAllJobDouments() {

        List<CourseDocument> courseDocuments = courseDocumentRepo.findAllOrderedByApplyDate (); // Use the new query
        List<CourseDocumentDto> courseDocumentDtos = new ArrayList<> ();

        for (CourseDocument courseDocument : courseDocuments) {

            CourseDocumentDto courseDocumentDto = new CourseDocumentDto (courseDocument.getCourseId (), courseDocument.getUsername (), courseDocument.getQualifications (), courseDocument.getAge (), courseDocument.getGender (), courseDocument.getImagePath (), courseDocument.getCvPath (), courseDocument.getApplyDate (), courseDocument.getId (), courseDocument.getNumber (), courseDocument.getUserEmail (), courseDocument.getAddress (), courseDocument.getUserid (), courseDocument.getCourseTitle ()

            );

            courseDocumentDtos.add (courseDocumentDto);
        }
        return courseDocumentDtos;
    }

    @Override
    public String deleteDoucment(int id) {
        if (courseDocumentRepo.existsById (id)) {
            courseDocumentRepo.deleteById (id);
            return "Job document deleted successfully";
        } else {
            return "Job document not deleted";
        }
    }

}
