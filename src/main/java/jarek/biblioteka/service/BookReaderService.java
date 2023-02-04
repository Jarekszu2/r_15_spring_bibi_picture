package jarek.biblioteka.service;

import jarek.biblioteka.model.bookReader.BookReader;
import jarek.biblioteka.model.bookReader.BookReaderSex;
import jarek.biblioteka.repository.BookReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookReaderService {

    @Autowired
    private BookReaderRepository bookReaderRepository;


    public void addBookReader(BookReader bookReader, String bookReaderSex) {
        bookReader.setBookReaderSex(BookReaderSex.valueOf(bookReaderSex));
        bookReaderRepository.save(bookReader);
    }

    public Page<BookReader> getPage(PageRequest of) {
        return bookReaderRepository.findAll(of);
    }

    public void deleteBookReader(Long removed_id) {
        bookReaderRepository.deleteById(removed_id);
    }

    public Optional<BookReader> findByID(Long edited_id) {
        return bookReaderRepository.findById(edited_id);
    }
}
