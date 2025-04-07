package lk.joblk.Joblk.repo;

import lk.joblk.Joblk.entity.JobDetails;
import lk.joblk.Joblk.entity.JobDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobDocumentRepo extends JpaRepository<JobDocument, Integer> {
    Optional<JobDocument> findById(int id);

    @Query("SELECT j FROM JobDocument j ORDER BY j.applyDate DESC")
    List<JobDocument> findAllOrderedByApplyDate();

}
