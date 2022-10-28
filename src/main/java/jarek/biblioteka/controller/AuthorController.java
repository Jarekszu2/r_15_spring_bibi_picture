package jarek.biblioteka.controller;

import jarek.biblioteka.model.Author;
import jarek.biblioteka.model.Book;
import jarek.biblioteka.service.AuthorService;
import jarek.biblioteka.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/author/")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @GetMapping(path = "/add")
    public String addForm(Model model, Author author) {
        model.addAttribute("atr_author", author);

        return "author-add";
    }

    @PostMapping("/add")
    public String postAuthor(Author author) {
        authorService.addAuthor(author);

        return "redirect:/author/list";
    }

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Author> authorPage = authorService.getPage(PageRequest.of(page, size));

        model.addAttribute("atr_authors", authorPage);

        return "author-list";
    }

    @GetMapping("/books/{id}")
    public String addAuthorsToBooks(Model model,
                                    @PathVariable("id") Long authorId) {
        Optional<Author> optionalAuthor = authorService.getAuthor(authorId);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();

            List<Book> bookList = bookService.getAll();

            model.addAttribute("atr_author", author);
            model.addAttribute("atr_books", bookList);

            return "author-bookform";
        }
        return "redirect:/author/list";
    }

    @GetMapping("/book/remove/{bookId}/{authorId}")
    public String removeBookFromAuthor(@PathVariable("bookId") Long book_Id,
                                       @PathVariable("authorId") Long author_Id,
                                       HttpServletRequest request) {
        authorService.removeBookFromAuthor(book_Id, author_Id);

        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping("/addBook")
    public String addBookToAuthor(Long authorId, Long bookId, HttpServletRequest request) {
        authorService.addBookToAuthor(authorId, bookId);

        return "redirect:" + request.getHeader("referer");
    }
}
