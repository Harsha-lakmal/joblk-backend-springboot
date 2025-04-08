package lk.joblk.Joblk.repo;

import lk.joblk.Joblk.entity.AcceptJobDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptJobDocumentRepo extends JpaRepository<AcceptJobDocument, Long> {
}
