package lk.joblk.Joblk.repo;


import lk.joblk.Joblk.entity.AcceptJobDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcceptJobDocumentRepo extends JpaRepository<AcceptJobDocument, Integer> {
    @Query("SELECT j FROM AcceptJobDocument j ORDER BY j.applyDate DESC")
    List<AcceptJobDocument> fillAllJobAcceptDocumentsOderBy();


}
