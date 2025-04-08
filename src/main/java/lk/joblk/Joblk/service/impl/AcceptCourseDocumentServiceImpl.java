package lk.joblk.Joblk.service.impl;

import lk.joblk.Joblk.entity.AcceptCourseDocument;
import lk.joblk.Joblk.entity.Course;
import lk.joblk.Joblk.entity.CourseDocument;
import lk.joblk.Joblk.repo.AcceptCourseDocumentServiceRepo;
import lk.joblk.Joblk.repo.CourseRepo;
import lk.joblk.Joblk.service.AcceptCourseDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AcceptCourseDocumentServiceImpl  implements AcceptCourseDocumentService {
    @Autowired
    AcceptCourseDocumentServiceRepo acceptCourseDocumentServiceRepo;


    public AcceptCourseDocument saveAcceptDocument(AcceptCourseDocument acceptCourseDocument) {

        return acceptCourseDocumentServiceRepo.save(acceptCourseDocument);


    }
}
