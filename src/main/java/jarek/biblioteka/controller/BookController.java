package jarek.biblioteka.controller;

import jarek.biblioteka.model.Book;
import jarek.biblioteka.service.BookService;
import jarek.biblioteka.service.PublishingHouseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/book/")
public class BookController {

    private final BookService bookService;
    private final PublishingHouseService publishingHouseService;

    @GetMapping(path = "/list")
    public String list(Model model) {
        List<Book> bookList = bookService.getAll();
        model.addAttribute("at_books", bookList);
        return "book-list";
    }

    @GetMapping(path = "/add")
    public String add(Model model, Book book) {
//        book.setNumberOfAvailableCopies(1);
        book.setYearWritten(LocalDate.now().getYear());
        model.addAttribute("at_book", book);
        model.addAttribute("at_PH_List", publishingHouseService.getAll());

        return "book-add-2";
    }

    @PostMapping(path = "/add")
    public String add(Book book, Long pub_HouseId) {
        bookService.save(book, pub_HouseId);

        return "redirect:/book/list";
    }

    @GetMapping(path = "/edit/{id}")
    public String edit(Model model,
                       @PathVariable(name = "id") Long editedBookId) {
        Optional<Book> optionalBook = bookService.getById(editedBookId);
        if (optionalBook.isPresent()) {
            model.addAttribute("at_book", optionalBook.get());
            model.addAttribute("at_PH_List", publishingHouseService.getAll());

            return "book-add-2";
        }
        return "redirect:/book/list";
    }

    @GetMapping(path = "/remove/{deletedId}")
    public String removeBook(HttpServletRequest request, @PathVariable(name = "deletedId") Long deleted_Id) {
        String referer = request.getHeader("referer");
        bookService.removeById(deleted_Id);

        if (referer != null) {
            return "redirect:" + referer;
        }

        return "redirect:/book/list";
    }

    @GetMapping(path = "/details/{bookId}")
    public String details(Model model,
                          HttpServletRequest request,
                          @PathVariable(name = "bookId") Long bookDetailsId) {
        Optional<Book> optionalBook = bookService.getById(bookDetailsId);
        if (optionalBook.isPresent()) {
            model.addAttribute("at_referer", request.getHeader("referer"));
            model.addAttribute("at_book", optionalBook.get());

            return "book-details";
        }
        return "redirect:/book/list";
    }
}
