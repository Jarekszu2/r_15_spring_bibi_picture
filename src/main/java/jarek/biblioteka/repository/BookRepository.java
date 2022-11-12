package jarek.biblioteka.repository;

import jarek.biblioteka.model.Book;
import jarek.biblioteka.model.Library;
import jarek.biblioteka.model.PublishingHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByTitle(String title);
    List<Book> findAllByYearWritten(int yearWritten);
    List<Book> findAllByPublishingHouse(PublishingHouse publishingHouse);
    List<Book> findAllByLibrary(Library library);
}
