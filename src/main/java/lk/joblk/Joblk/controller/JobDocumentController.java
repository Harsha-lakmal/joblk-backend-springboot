package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.dto.JobDocumentDto;
import lk.joblk.Joblk.dto.ResponseDto;
import lk.joblk.Joblk.entity.JobDocument;
import lk.joblk.Joblk.repo.JobDocumentRepo;
import lk.joblk.Joblk.service.JobDocumentService;
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


@RestController
@RequestMapping("api/v1/job")
public class JobDocumentController {

    @Autowired
    JobDocumentRepo jobDocumentRepo;
    @Autowired
    private JobDocumentService jobDocumentService;
    @Autowired
    private ResponseDto responseDto;

    @PostMapping("/saveJobDocument/{jobId}")
    public ResponseEntity<String> saveJobDocument(@PathVariable int jobId, @RequestBody JobDocumentDto jobDocumentDto) {
        JobDocumentDto savedDoc = jobDocumentService.saveJobDocument (jobDocumentDto, jobId);

        return ResponseEntity.ok ("Job document saved successfully with ID: " + savedDoc.getId ());
    }


    @GetMapping("/getAllJobDocuments")
    public ResponseEntity<List<JobDocumentDto>> getAllJobsDetails() {
        List<JobDocumentDto> allDetailsJobs = jobDocumentService.getAllJobDouments ();
        if (allDetailsJobs.isEmpty ()) {
            return ResponseEntity.noContent ().build ();
        }
        return ResponseEntity.ok (allDetailsJobs);
    }


    @DeleteMapping("/deleteDocument/{id}")
    public String deleteDocument(@PathVariable int id) {
        String res = jobDocumentService.deleteDoucment (id);


        return "Job document deleted successfully " + id;

    }

    //Cv document upload for Jobs


    @PostMapping("/uploadCvDocument/{id}")
    public ResponseEntity<String> addFiles(@RequestParam("file") MultipartFile file, @PathVariable int id) {
        try {
            String result = jobDocumentService.saveFile (file, id);

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


    @GetMapping("/getCvDocument/{id}")
    public ResponseEntity<byte[]> getCvDocument(@PathVariable("id") int id) {
        try {
            byte[] cv = jobDocumentService.getCvDocument (id);

            // Extract the file extension to determine the content type
            String fileExtension = jobDocumentService.getFileExtension (id);
            if (fileExtension == null || fileExtension.isEmpty ()) {
                return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
            }

            // Determine the correct media type based on file extension
            MediaType mediaType = jobDocumentService.getMediaTypeForFileExtension (fileExtension);

            HttpHeaders headers = new HttpHeaders ();
            headers.setContentType (mediaType);

            return new ResponseEntity<> (cv, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle exception gracefully
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
    }


    //image upload  Job Document for image

    @PostMapping("/uploadDocumentImage/{id}")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file, @PathVariable int id) throws IOException {
        int i = jobDocumentService.imageUpload (file, id);

        if (i == 1) {
            return new ResponseEntity<String> ("Upload Success !", HttpStatus.CREATED);
        }
        return new ResponseEntity<> ("Upload Failed", HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/getDocumentImage/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) throws IOException {

        Optional<JobDocument> jobDocumentOptional = jobDocumentRepo.findById (id);
        if (!jobDocumentOptional.isPresent ()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }

        JobDocument jobDocument = jobDocumentOptional.get ();
        String imgUrl = jobDocument.getImagePath ();

        String fileExtension = getFileExtension (imgUrl);
        if (fileExtension == null || fileExtension.isEmpty ()) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }

        byte[] image;
        try {
            image = jobDocumentService.getImage (id);
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

    @DeleteMapping("/deleteDocumentImage/{id}")
    public String imageDelete(@PathVariable int id) {
        String s = jobDocumentService.deleteImage (id);
        return s;
    }

    @PutMapping("/updateDocumentImage/{id}")
    public String updateImage(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
        String s = jobDocumentService.updateImage (id, file);
        return s;

    }


}
