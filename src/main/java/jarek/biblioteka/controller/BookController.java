package jarek.biblioteka.controller;

import jarek.biblioteka.model.Book;
import jarek.biblioteka.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/book/")
public class BookController {

    private final BookService bookService;

    @GetMapping(path = "/list")
    public String list(Model model) {
        List<Book> bookList = bookService.getAll();
        model.addAttribute("at_books", bookList);
        return "book-list";
    }
}
