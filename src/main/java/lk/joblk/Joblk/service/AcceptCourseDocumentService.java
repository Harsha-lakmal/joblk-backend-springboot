package lk.joblk.Joblk.service;

import lk.joblk.Joblk.entity.AcceptCourseDocument;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptCourseDocumentService {
    AcceptCourseDocument saveAcceptDocument(AcceptCourseDocument acceptCourseDocument);
}
