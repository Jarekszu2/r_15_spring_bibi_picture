package jarek.biblioteka.service;

import jarek.biblioteka.exception.WrongOperation;
import jarek.biblioteka.model.Book;
import jarek.biblioteka.model.PublishingHouse;
import jarek.biblioteka.model.StatusLibrary;
import jarek.biblioteka.repository.BookRepository;
import jarek.biblioteka.repository.PublishingHouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final PublishingHouseRepository publishingHouseRepository;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public void save(Book book, Long pub_houseId) {
        if (publishingHouseRepository.existsById(pub_houseId)) {
            PublishingHouse publishingHouse = publishingHouseRepository.getById(pub_houseId);
            book.setPublishingHouse(publishingHouse);
            book.setStatusLibrary(StatusLibrary.AVAILABLE);
            bookRepository.save(book);
        } else {
            throw new EntityNotFoundException("Publishing house not found.");
        }
    }

    public Optional<Book> getById(Long editedBookId) {
        return bookRepository.findById(editedBookId);
    }

    public void removeById(Long deleted_id) {
//        bookRepository.deleteById(deleted_id);

        Optional<Book> optionalBook = bookRepository.findById(deleted_id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.getAuthors() == null) {
                bookRepository.delete(book);
            } else {
                throw new WrongOperation("Can not remove book with assigned authors!");
            }
        }
    }


    public void saveBook(Book book, Long phId) {
        Optional<PublishingHouse> optionalPublishingHouse = publishingHouseRepository.findById(phId);
        if (optionalPublishingHouse.isPresent()) {

            PublishingHouse publishingHouse = optionalPublishingHouse.get();
            book.setPublishingHouse(publishingHouse);
            book.setStatusLibrary(StatusLibrary.AVAILABLE);
            bookRepository.save(book);
        }
    }
}
