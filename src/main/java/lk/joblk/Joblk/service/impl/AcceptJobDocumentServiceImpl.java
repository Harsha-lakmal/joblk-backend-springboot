package lk.joblk.Joblk.service.impl;


import lk.joblk.Joblk.entity.AcceptJobDocument;
import lk.joblk.Joblk.repo.AcceptJobDocumentRepo;
import lk.joblk.Joblk.service.AcceptJobDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AcceptJobDocumentServiceImpl implements AcceptJobDocumentService {
    @Autowired
    AcceptJobDocumentRepo acceptJobDocumentRepo;

    public AcceptJobDocument saveAcceptDocument(AcceptJobDocument acceptJobDocument) {

        return acceptJobDocumentRepo.save(acceptJobDocument);


    }
}
