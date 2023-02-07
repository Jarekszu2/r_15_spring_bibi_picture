package jarek.biblioteka.controller;

import jarek.biblioteka.model.Book;
import jarek.biblioteka.model.bookLent.BookLent;
import jarek.biblioteka.service.BookLentService;
import jarek.biblioteka.service.BookReaderService;
import jarek.biblioteka.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/bookLent/")
public class BookLentController {

    @Autowired
    private BookReaderService bookReaderService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookLentService bookLentService;

    @GetMapping(path = "/add")
    public String addBookLent(Model model, BookLent bookLent) {

        model.addAttribute("atr_bookLent", bookLent);
        model.addAttribute("atr_bookReaderList", bookReaderService.getAll());

        List<Book> notBorrowedBooks = bookService.getAll().stream()
                .filter(book -> book.getBookLent() == null)
                .collect(Collectors.toList());

        model.addAttribute("atr_bookList", notBorrowedBooks);

        return "bookLent-add";
    }

    @PostMapping(path = "/add")
    public String addBookLent(BookLent bookLent, Long bookId, Long bookReaderId) {

        bookLentService.addBookLent(bookLent, bookId, bookReaderId);

        return "redirect:/bookLent/list";
    }

    @GetMapping(path = "/list")
    public String listBookLents(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "5") int size) {

        Page<BookLent> bookLentPage = bookLentService.getPage(PageRequest.of(page, size));

        model.addAttribute("atr_BookLentList", bookLentPage);

        return "bookLent-list";
    }

    @GetMapping(path = "/remove/{id}")
    public String removeBookLent(@PathVariable(name = "id") Long removed_Id) {

        bookLentService.removeBookLent(removed_Id);

        return "redirect:/bookLent/list";
    }
}
