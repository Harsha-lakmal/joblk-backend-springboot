package lk.joblk.Joblk.repo;

import lk.joblk.Joblk.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c ORDER BY c.dateUpload DESC")
    List<Course> findAllOrderedByDateUpload();


}
