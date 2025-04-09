package lk.joblk.Joblk.service;

import lk.joblk.Joblk.dto.AcceptJobDocumentDto;
import lk.joblk.Joblk.entity.AcceptJobDocument;

import java.util.List;

public interface AcceptJobDocumentService {
    AcceptJobDocument saveAcceptDocument(AcceptJobDocument acceptJobDocument);

    List<AcceptJobDocumentDto> getAllJobDocumentDetails();

    String deleteJobAcceptDocumentDetails(int id);
}
