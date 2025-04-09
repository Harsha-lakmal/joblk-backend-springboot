package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.dto.AcceptJobDocumentDto;
import lk.joblk.Joblk.entity.AcceptJobDocument;
import lk.joblk.Joblk.service.AcceptJobDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/job")
public class AcceptJobDocumentController {
    @Autowired
     AcceptJobDocumentService acceptJobDocumentService;

    @PostMapping("/saveAcceptDocumentJob")
    public ResponseEntity<AcceptJobDocument> saveAcceptDocument(@RequestBody AcceptJobDocument acceptJobDocument) {
        try {
            AcceptJobDocument acceptJobDocuments = acceptJobDocumentService.saveAcceptDocument (acceptJobDocument);
            return new ResponseEntity<> (acceptJobDocuments, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<> (null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getAcceptJobDocument")
    public ResponseEntity<List<AcceptJobDocumentDto>> getAcceptJobDocument() {
        try {
            List<AcceptJobDocumentDto> allJobDocumentDetails = acceptJobDocumentService.getAllJobDocumentDetails ();
            return new ResponseEntity<> (allJobDocumentDetails, HttpStatus.OK);

        }
        catch (Exception e) {
            return new ResponseEntity<> (null, HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/deleteAcceptJobDocument/{id}")
    public ResponseEntity<String> deleteAcceptJobDocument(@PathVariable int id ) {
        try {
            acceptJobDocumentService.deleteJobAcceptDocumentDetails(id);
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<> ("Fail to delete ", HttpStatus.BAD_REQUEST);
        }


    }

}
