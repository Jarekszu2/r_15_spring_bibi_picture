package jarek.biblioteka.service;

import jarek.biblioteka.exception.WrongOperation;
import jarek.biblioteka.model.Book;
import jarek.biblioteka.model.Library;
import jarek.biblioteka.model.StatusLibrary;
import jarek.biblioteka.repository.BookRepository;
import jarek.biblioteka.repository.LibraryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Library> getAll() {
        return libraryRepository.findAll();
    }

    public void addLibrary(Library library) {
        libraryRepository.save(library);
    }

    public Optional<Library> getLibrary(Long libraryId) {
        return libraryRepository.findById(libraryId);
    }

    public void removeLibrary(Long deletedId) {

        if (!libraryRepository.existsById(deletedId)) {
            throw new WrongOperation("Library not found.");
        }

        Library library = libraryRepository.getById(deletedId);

        if (library.getBookList().size() != 0) {
            throw new WrongOperation("Can not remove a library with assigned books.");
        }

        libraryRepository.deleteById(deletedId);
    }

    public void addBookToLibrary(Long libraryId, Long bookId) {

        if (!libraryRepository.existsById(libraryId)) {
            throw new WrongOperation("Library not found.");
        }

        Library library = libraryRepository.getById(libraryId);

        if (!bookRepository.existsById(bookId)) {
            throw new WrongOperation("Book not found.");
        }

        Book book = bookRepository.getById(bookId);

        book.setLibrary(library);
        book.setStatusLibrary(StatusLibrary.LIBRARY);
        bookRepository.save(book);

//        library.getBookList().add(book);
//        libraryRepository.addBookReader(library);
    }

    public void removeBookFromLibrary(Long library_id, Long book_id) {

        if (!libraryRepository.existsById(library_id)) {
            throw new WrongOperation("Library not found.");
        }

        if (!bookRepository.existsById(book_id)) {
            throw new WrongOperation("Book not found.");
        }

        Library library = libraryRepository.getById(library_id);
        Book book = bookRepository.getById(book_id);
//
//        library.getBookList().remove(book);
//
//        libraryRepository.addBookReader(library);

        book.setLibrary(null);
        book.setStatusLibrary(StatusLibrary.AVAILABLE);
        bookRepository.save(book);
    }
}
