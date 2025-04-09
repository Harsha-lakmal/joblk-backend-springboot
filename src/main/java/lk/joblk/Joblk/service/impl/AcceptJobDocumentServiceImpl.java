package lk.joblk.Joblk.service.impl;


import lk.joblk.Joblk.dto.AcceptJobDocumentDto;
import lk.joblk.Joblk.entity.AcceptJobDocument;
import lk.joblk.Joblk.repo.AcceptJobDocumentRepo;
import lk.joblk.Joblk.service.AcceptJobDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AcceptJobDocumentServiceImpl implements AcceptJobDocumentService {
    @Autowired
    AcceptJobDocumentRepo acceptJobDocumentRepo;

    public AcceptJobDocument saveAcceptDocument(AcceptJobDocument acceptJobDocument) {

        return acceptJobDocumentRepo.save (acceptJobDocument);


    }

    @Override
    public List<AcceptJobDocumentDto> getAllJobDocumentDetails() {
        List<AcceptJobDocument> acceptJobDocuments = acceptJobDocumentRepo.fillAllJobAcceptDocumentsOderBy ();
        List<AcceptJobDocumentDto> acceptJobDocumentDtoDetails = new ArrayList<> ();

        for (AcceptJobDocument acceptJobDocument : acceptJobDocuments) {

            AcceptJobDocumentDto acceptJobDocumentDto = new AcceptJobDocumentDto (
                    acceptJobDocument.getJobId (),
                    acceptJobDocument.getJobTitle (),
                    acceptJobDocument.getJobDocumentId ()
                    , acceptJobDocument.getUserEmail (),
                    acceptJobDocument.getUserId (),
                    acceptJobDocument.getAddress (),
                    acceptJobDocument.getApplyDate (),
                    acceptJobDocument.getAge (),
                    acceptJobDocument.getGender (),
                    acceptJobDocument.getId (),
                    acceptJobDocument.getUsername (),
                    acceptJobDocument.getNumber (),
                    acceptJobDocument.getQualifications ()
            );

            acceptJobDocumentDtoDetails.add (acceptJobDocumentDto);


        }



        return acceptJobDocumentDtoDetails;
    }

    @Override
    public String deleteJobAcceptDocumentDetails(int id) {
        if (acceptJobDocumentRepo.existsById(id)) {
            acceptJobDocumentRepo.deleteById(id);
            return "success to delete "+id;
        }else {
            return "fail to delete id is not found "+id;
        }

    }
}
