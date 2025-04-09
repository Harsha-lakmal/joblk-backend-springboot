package lk.joblk.Joblk.service;

import lk.joblk.Joblk.dto.AcceptCourseDocumentDto;
import lk.joblk.Joblk.dto.AcceptJobDocumentDto;
import lk.joblk.Joblk.entity.AcceptCourseDocument;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcceptCourseDocumentService {
    AcceptCourseDocument saveAcceptDocument(AcceptCourseDocument acceptCourseDocument);

    List<AcceptCourseDocumentDto> getAllCourseDocumentDetails();

    String deleteCourseAcceptDocumentDetails(int id);
}
