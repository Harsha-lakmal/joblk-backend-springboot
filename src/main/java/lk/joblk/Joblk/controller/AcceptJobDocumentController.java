package lk.joblk.Joblk.controller;

import lk.joblk.Joblk.entity.AcceptJobDocument;
import lk.joblk.Joblk.service.AcceptJobDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
