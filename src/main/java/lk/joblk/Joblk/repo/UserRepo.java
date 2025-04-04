package lk.joblk.Joblk.repo;


import lk.joblk.Joblk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u ORDER BY u.registerDate DESC")
    List<User> findAllDetails();


    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);


}
