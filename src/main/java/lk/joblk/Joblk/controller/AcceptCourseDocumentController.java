package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.dto.AcceptCourseDocumentDto;

import lk.joblk.Joblk.entity.AcceptCourseDocument;
import lk.joblk.Joblk.service.AcceptCourseDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/course")
public class AcceptCourseDocumentController {
    @Autowired
    AcceptCourseDocumentService acceptCourseDocumentService;
    @PostMapping("/saveAcceptDocument")
    public ResponseEntity<AcceptCourseDocument> saveAcceptDocument(@RequestBody AcceptCourseDocument acceptCourseDocument) {
        try {
            AcceptCourseDocument acceptCourseDocuments = acceptCourseDocumentService.saveAcceptDocument (acceptCourseDocument);
            return new ResponseEntity<> (acceptCourseDocuments, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<> (null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAcceptCourseDocument")
    public ResponseEntity<List<AcceptCourseDocumentDto>> getAcceptCourseDocument() {
        try {
            List<AcceptCourseDocumentDto> allCourseDocumentDetails = acceptCourseDocumentService.getAllCourseDocumentDetails ();
            return new ResponseEntity<> (allCourseDocumentDetails, HttpStatus.OK);

        }
        catch (Exception e) {
            return new ResponseEntity<> (null, HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/deleteAcceptCourseDocument/{id}")
    public ResponseEntity<String> deleteAcceptCourseDocument(@PathVariable int id ) {
        try {
            acceptCourseDocumentService.deleteCourseAcceptDocumentDetails(id);

            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<> ("Fail to delete ", HttpStatus.BAD_REQUEST);
        }


    }

}
