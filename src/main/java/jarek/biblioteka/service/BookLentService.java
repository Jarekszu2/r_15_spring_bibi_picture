package jarek.biblioteka.service;

import jarek.biblioteka.exception.WrongOperation;
import jarek.biblioteka.model.Book;
import jarek.biblioteka.model.bookLent.BookLent;
import jarek.biblioteka.model.bookReader.BookReader;
import jarek.biblioteka.repository.BookLentRepository;
import jarek.biblioteka.repository.BookReaderRepository;
import jarek.biblioteka.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookLentService {

    @Autowired
    private BookLentRepository bookLentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookReaderRepository bookReaderRepository;

    public void addBookLent(BookLent bookLent, Long bookId, Long bookReaderId) {

        if (!bookRepository.existsById(bookId)) {
            throw new WrongOperation("Book not found.");
        }
        Book book = bookRepository.getById(bookId);

        if (!bookReaderRepository.existsById(bookReaderId)) {
            throw new WrongOperation("BookReaderNotFound.");
        }
        BookReader bookReader = bookReaderRepository.getById(bookReaderId);

        bookLent.setBook(book);
        bookLent.setBookReader(bookReader);
        bookLent.setBookReturnDate(LocalDate.now().plusMonths(1));

        bookLentRepository.save(bookLent);
    }

    public List<BookLent> getAll() {
        return bookLentRepository.findAll();
    }

    public void removeBookLent(Long removed_id) {
        bookLentRepository.deleteById(removed_id);
    }

    public Page<BookLent> getPage(PageRequest of) {
        return bookLentRepository.findAll(of);
    }
}
