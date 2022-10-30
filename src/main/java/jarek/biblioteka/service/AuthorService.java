package jarek.biblioteka.service;

import jarek.biblioteka.model.Author;
import jarek.biblioteka.model.Book;
import jarek.biblioteka.repository.AuthorRepository;
import jarek.biblioteka.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    public Page<Author> getPage(PageRequest of) {
        return authorRepository.findAll(of);
    }

    public Optional<Author> getAuthor(Long authorId) {
        return authorRepository.findById(authorId);
    }

    public void addBookToAuthor(Long authorId, Long bookId) {

        if (!authorRepository.existsById(authorId)) {
            return;
        }

        if (!bookRepository.existsById(bookId)) {
            return;
        }

        Author author = authorRepository.getById(authorId);
        Book book = bookRepository.getById(bookId);

        /*czy dodać autorowi książkę czy książce autora?
        * ponieważ relacja jest: Book "mappedBy", dodajemy po stronie Author */
        author.getBooks().add(book);
        authorRepository.save(author);
    }

    public void removeBookFromAuthor(Long book_id, Long author_id) {

        if (!authorRepository.existsById(author_id)) {
            return;
        }
        Author author = authorRepository.getById(author_id);

        if (!bookRepository.existsById(book_id)) {
            return;
        }
        Book book = bookRepository.getById(book_id);

        author.getBooks().remove(book);
        authorRepository.save(author);
    }

    public void deleteAuthor(Long deletedId) {
        Optional<Author> optionalAuthor = authorRepository.findById(deletedId);
        if (optionalAuthor.isPresent()) {
            authorRepository.delete(optionalAuthor.get());
        } else {
            throw new EntityNotFoundException("Author not found");
        }
    }
}
