package jarek.biblioteka.repository;

import jarek.biblioteka.model.bookReader.BookReader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReaderRepository extends JpaRepository<BookReader, Long> {
}
