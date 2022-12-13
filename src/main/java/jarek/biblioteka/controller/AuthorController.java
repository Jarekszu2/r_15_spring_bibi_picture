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
import org.springframework.web.multipart.MultipartFile;

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
                                    HttpServletRequest request,
                                    @PathVariable("id") Long authorId) {
        Optional<Author> optionalAuthor = authorService.getAuthor(authorId);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();

            List<Book> bookList = bookService.getAll();

            model.addAttribute("atr_author", author);
            model.addAttribute("atr_books", bookList);
            model.addAttribute("atr_referer", request.getHeader("referer"));

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

    @GetMapping("/edit/{id}")
    public String editAuthor(Model model,
                             @PathVariable(name = "id") Long editedId) {
        Optional<Author> optionalAuthor = authorService.getAuthor(editedId);
        if (optionalAuthor.isPresent()) {
            model.addAttribute("atr_author", optionalAuthor.get());

            return "author-add";
        }
        return "redirect:/author/list";
    }

    @GetMapping("/remove/{id}")
    public String deleteAuthor(@PathVariable(name = "id") Long deletedId) {

        authorService.deleteAuthor(deletedId);

        return "redirect:/author/list";
    }

    @PostMapping("/addBook")
    public String addBookToAuthor(Long authorId, Long bookId, HttpServletRequest request) {
        authorService.addBookToAuthor(authorId, bookId);

        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/profilePhoto/{authorId}")
    public String profilePhoto(Model model,
                               HttpServletRequest request,
                               @PathVariable(name = "authorId") Long authorProfilePhotoId) {

        Optional<Author> optionalAuthor = authorService.getAuthor(authorProfilePhotoId);
        if (optionalAuthor.isPresent()) {
            model.addAttribute("atr_author", optionalAuthor.get());
            model.addAttribute("atr_referer", request.getHeader("referer"));

            return "photo-form";
        }
        return "redirect:/author/list";
    }

    @PostMapping("/profilePhoto")
    public String uploadPhoto(Long authorForPhotoId,
                              @RequestParam("photo") MultipartFile photo) {

        authorService.savePhotoFor(authorForPhotoId, photo);

        return "redirect:/author/list";
    }

    @GetMapping("/photo/{authorId}")
    public String photo(Model model,
                        HttpServletRequest request,
                        @PathVariable(name = "authorId") Long authorWithPhotoId) {

        Optional<Author> optionalAuthor = authorService.getAuthor(authorWithPhotoId);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();


            model.addAttribute("atr_author", author);
            model.addAttribute("atr_referer", request.getHeader("referer"));
            model.addAttribute("atr_img", author.convertBinImageToString());
            return "author-photo";
        }
        return "redirect:/author/list";
    }
}
