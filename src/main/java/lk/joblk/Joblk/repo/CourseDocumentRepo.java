package lk.joblk.Joblk.repo;

import lk.joblk.Joblk.entity.CourseDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseDocumentRepo extends JpaRepository<CourseDocument, Integer> {
     Optional<CourseDocument> findById(int id);

    @Query("SELECT j FROM CourseDocument j ORDER BY j.applyDate DESC")
    List<CourseDocument> findAllOrderedByApplyDate();

}
