package jarek.biblioteka.repository;

import jarek.biblioteka.model.BookSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSearchRepository extends JpaRepository<BookSearch, Long> {
}
