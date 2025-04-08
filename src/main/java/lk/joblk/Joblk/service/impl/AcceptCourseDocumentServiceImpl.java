package lk.joblk.Joblk.service.impl;

import lk.joblk.Joblk.entity.AcceptCourseDocument;
import lk.joblk.Joblk.repo.AcceptCourseDocumentRepo;
import lk.joblk.Joblk.service.AcceptCourseDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AcceptCourseDocumentServiceImpl  implements AcceptCourseDocumentService {
    @Autowired
    AcceptCourseDocumentRepo acceptCourseDocumentRepo;


    public AcceptCourseDocument saveAcceptDocument(AcceptCourseDocument acceptCourseDocument) {

        return acceptCourseDocumentRepo.save(acceptCourseDocument);


    }
}
