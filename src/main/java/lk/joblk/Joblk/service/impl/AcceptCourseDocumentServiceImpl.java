package lk.joblk.Joblk.service.impl;

import lk.joblk.Joblk.dto.AcceptCourseDocumentDto;
import lk.joblk.Joblk.entity.AcceptCourseDocument;
import lk.joblk.Joblk.repo.AcceptCourseDocumentRepo;
import lk.joblk.Joblk.service.AcceptCourseDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class AcceptCourseDocumentServiceImpl  implements AcceptCourseDocumentService {
    @Autowired
    AcceptCourseDocumentRepo acceptCourseDocumentRepo;


    public AcceptCourseDocument saveAcceptDocument(AcceptCourseDocument acceptCourseDocument) {

        return acceptCourseDocumentRepo.save(acceptCourseDocument);

    }

    @Override
    public List<AcceptCourseDocumentDto> getAllCourseDocumentDetails() {
        List<AcceptCourseDocument> acceptJobDocuments = acceptCourseDocumentRepo.fillAllCourseAcceptDocumentsOderBy ();
        List<AcceptCourseDocumentDto> acceptJobDocumentDtoDetails = new ArrayList<> ();

        for (AcceptCourseDocument acceptCourseDocument : acceptJobDocuments) {

            AcceptCourseDocumentDto acceptJobDocumentDto = new AcceptCourseDocumentDto (
                    acceptCourseDocument.getCourseId (),
                    acceptCourseDocument.getCourseTitle (),
                    acceptCourseDocument.getCourseDocumentId ()
                    , acceptCourseDocument.getUserEmail (),
                    acceptCourseDocument.getUserId (),
                    acceptCourseDocument.getAddress (),
                    acceptCourseDocument.getApplyDate (),
                    acceptCourseDocument.getAge (),
                    acceptCourseDocument.getGender (),
                    acceptCourseDocument.getId (),
                    acceptCourseDocument.getUsername (),
                    acceptCourseDocument.getNumber (),
                    acceptCourseDocument.getQualifications ()
            );

            acceptJobDocumentDtoDetails.add (acceptJobDocumentDto);


        }



        return acceptJobDocumentDtoDetails;
    }

    @Override
    public String deleteCourseAcceptDocumentDetails(int id) {
        if (acceptCourseDocumentRepo.existsById(id)) {
            acceptCourseDocumentRepo.deleteById(id);
            return "success to delete "+id;
        }else {
            return "fail to delete id is not found "+id;
        }

    }
}
