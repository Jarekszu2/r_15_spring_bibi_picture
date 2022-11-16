package jarek.biblioteka.repository;

import jarek.biblioteka.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByTitle(String title);
    List<Book> findAllByYearWritten(int yearWritten);
    List<Book> findAllByPublishingHouse(PublishingHouse publishingHouse);
    List<Book> findAllByLibrary(Library library);
    List<Book> findAllByStatusLibrary(StatusLibrary statusLibrary);
    List<Book> findAllByAuthors(Author author);
    List<Book> findAllByAuthorsAndLibrary(Author author, Library library);
    List<Book> findAllByLibraryAndAuthors(Library library, Author author);
}
