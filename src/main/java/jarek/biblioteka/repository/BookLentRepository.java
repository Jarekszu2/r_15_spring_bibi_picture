package jarek.biblioteka.repository;

import jarek.biblioteka.model.bookLent.BookLent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLentRepository extends JpaRepository<BookLent, Long> {
}
