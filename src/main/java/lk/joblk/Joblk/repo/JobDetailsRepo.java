package lk.joblk.Joblk.repo;


import lk.joblk.Joblk.entity.JobDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDetailsRepo extends JpaRepository<JobDetails, Integer> {

}
