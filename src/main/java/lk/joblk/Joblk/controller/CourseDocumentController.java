package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.dto.CourseDocumentDto;
import lk.joblk.Joblk.entity.CourseDocument;
import lk.joblk.Joblk.repo.CourseDocumentRepo;
import lk.joblk.Joblk.service.CourseDocumentService;
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
@RequestMapping("api/v1/course")
public class CourseDocumentController {

    @Autowired
    CourseDocumentRepo courseDocumentRepo;
    @Autowired
    private CourseDocumentService courseDocumentService;

    @PostMapping("/saveCourseDocument/{courseID}")
    public ResponseEntity<String> saveCoursesDocument(@PathVariable int courseID, @RequestBody CourseDocumentDto courseDocumentDto) {
        CourseDocumentDto savedDoc = courseDocumentService.saveCourseDocument (courseDocumentDto, courseID);

        return ResponseEntity.ok ("Course document saved successfully with ID: " + savedDoc.getId ());
    }



    @PostMapping("/saveDocumentCourse")
    public ResponseEntity<CourseDocument> createCourseDocument(@RequestBody CourseDocument courseDocument) {
        try {
            CourseDocument createdJobDocument = courseDocumentService.createCourseDocuments (courseDocument);
            return new ResponseEntity<> (createdJobDocument, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<> (null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getAllCourseDocuments")
    public ResponseEntity<List<CourseDocumentDto>> getAllJobsDetails() {
        List<CourseDocumentDto> courseDocumentDto = courseDocumentService.getAllJobDouments ();
        if (courseDocumentDto.isEmpty ()) {
            return ResponseEntity.noContent ().build ();
        }
        return ResponseEntity.ok (courseDocumentDto);
    }


    @DeleteMapping("/deleteDocumentCourse/{id}")
    public String deleteDocument(@PathVariable int id) {
        String res = courseDocumentService.deleteDoucment (id);


        return "Course document deleted successfully " + id;

    }

    //Cv document upload for Jobs


    @PostMapping("/uploadCvDocumentCourse/{id}")
    public ResponseEntity<String> addFiles(@RequestParam("file") MultipartFile file, @PathVariable int id) {
        try {
            String result = courseDocumentService.saveFile (file, id);

            if ("00".equals (result)) {
                return new ResponseEntity<> ("Upload Success!", HttpStatus.CREATED);
            }
            return new ResponseEntity<> ("Upload Failed", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<> ("Unexpected error: " + e.getMessage (), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getFileExtension(String url) {
        if (url == null || !url.contains (".")) {
            return null;
        }
        return url.substring (url.lastIndexOf (".") + 1);
    }


    @GetMapping("/getCvDocumentCourse/{id}")
    public ResponseEntity<byte[]> getCvDocument(@PathVariable("id") int id) {
        try {
            byte[] cv = courseDocumentService.getCvDocument (id);

            // Extract the file extension to determine the content type
            String fileExtension = courseDocumentService.getFileExtension (id);
            if (fileExtension == null || fileExtension.isEmpty ()) {
                return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
            }

            // Determine the correct media type based on file extension
            MediaType mediaType = courseDocumentService.getMediaTypeForFileExtension (fileExtension);

            HttpHeaders headers = new HttpHeaders ();
            headers.setContentType (mediaType);

            return new ResponseEntity<> (cv, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle exception gracefully
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
    }


    //image upload  Job Document for image

    @PostMapping("/uploadDocumentImageCourse/{id}")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file, @PathVariable int id) throws IOException {
        int i = courseDocumentService.imageUpload (file, id);

        if (i == 1) {
            return new ResponseEntity<String> ("Upload Success !", HttpStatus.CREATED);
        }
        return new ResponseEntity<> ("Upload Failed", HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/getDocumentImageCourse/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) throws IOException {

        Optional<CourseDocument> courseDocumentOptional = courseDocumentRepo.findById (id);
        if (!courseDocumentOptional.isPresent ()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }

        CourseDocument courseDocument = courseDocumentOptional.get ();
        String imgUrl = courseDocument.getImagePath ();

        String fileExtension = getFileExtension (imgUrl);
        if (fileExtension == null || fileExtension.isEmpty ()) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }

        byte[] image;
        try {
            image = courseDocumentService.getImage (id);
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

    @DeleteMapping("/deleteDocumentImageCourse/{id}")
    public String imageDelete(@PathVariable int id) {
        String s = courseDocumentService.deleteImage (id);
        return s;
    }

    @PutMapping("/updateDocumentImageCourse/{id}")
    public String updateImage(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
        String s = courseDocumentService.updateImage (id, file);
        return s;

    }


}
