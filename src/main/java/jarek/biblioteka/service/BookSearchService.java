package jarek.biblioteka.service;

import jarek.biblioteka.model.BookSearch;
import jarek.biblioteka.repository.BookSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookSearchService {

    @Autowired
    private BookSearchRepository bookSearchRepository;

    public void save(BookSearch bookSearch) {
        bookSearchRepository.deleteAll();
        bookSearchRepository.save(bookSearch);
    }

    BookSearch getBookSearch() {
        List<BookSearch> bookSearchList = bookSearchRepository.findAll();
        return bookSearchList.get(0);
    }
}
