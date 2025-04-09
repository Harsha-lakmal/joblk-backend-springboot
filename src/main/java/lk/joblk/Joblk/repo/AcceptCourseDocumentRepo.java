package lk.joblk.Joblk.repo;

import lk.joblk.Joblk.entity.AcceptCourseDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcceptCourseDocumentRepo extends JpaRepository<AcceptCourseDocument, Integer> {

    @Query("SELECT j FROM AcceptCourseDocument j ORDER BY j.applyDate DESC")
    List<AcceptCourseDocument> fillAllCourseAcceptDocumentsOderBy();
}
