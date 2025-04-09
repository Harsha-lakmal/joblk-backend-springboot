package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.entity.AcceptCourseDocument;
import lk.joblk.Joblk.service.AcceptCourseDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
