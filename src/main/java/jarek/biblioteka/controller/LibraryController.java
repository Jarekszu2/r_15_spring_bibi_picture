package jarek.biblioteka.controller;

import jarek.biblioteka.model.Library;
import jarek.biblioteka.repository.BookRepository;
import jarek.biblioteka.service.BookService;
import jarek.biblioteka.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/library/")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/add")
    public String addLibrary(Model model, Library library) {

        model.addAttribute("atr_library", library);

        return "library-add";
    }

    @GetMapping("/list")
    public String listLibrary(Model model) {

        model.addAttribute("atr_libraryList", libraryService.getAll());

        return "library-list";
    }

    @GetMapping("/remove/{id}")
    public String removeLibrary(@PathVariable(name = "id") Long deletedId) {

        libraryService.removeLibrary(deletedId);

        return "redirect:/library/list";
    }

    @GetMapping("/edit/{id}")
    public String editLibrary(Model model,
                              @PathVariable(name = "id") Long editedId) {
        Optional<Library> optionalLibrary = libraryService.getLibrary(editedId);
        if (optionalLibrary.isPresent()) {

            model.addAttribute("atr_library", optionalLibrary.get());

            return "library-add";
        }

        return "redirect:/library/list";
    }

    @GetMapping("/books/{id}")
    public String listLibraryBooks(Model model,
                                   HttpServletRequest request,
                                   @PathVariable(name = "id") Long libraryId) {

        Optional<Library> optionalLibrary = libraryService.getLibrary(libraryId);
        if (optionalLibrary.isPresent()) {

//            model.addAttribute("atr_listBooks", optionalLibrary.get().getBookList());
            model.addAttribute("atr_listBooks", bookRepository.findAllByLibrary(optionalLibrary.get()));
            model.addAttribute("atr_library", optionalLibrary.get());
            model.addAttribute("atr_allBooks", bookService.getAllAvailable());
            model.addAttribute("atr_referer", request.getHeader("referer"));

            return "library-books";
        }

        return "redirect:/library/list";
    }

    @GetMapping("/removeBook/{libraryId}/{bookId}")
    public String removeBookFromLibrary(@PathVariable(name = "libraryId") Long library_Id,
                                        @PathVariable(name = "bookId") Long book_Id,
                                        HttpServletRequest request) {

        libraryService.removeBookFromLibrary(library_Id, book_Id);

        return "redirect:" + request.getHeader("referer");
    }

    @PostMapping("/add")
    public String addLibrary(Library library) {

        libraryService.addLibrary(library);

        return "redirect:/library/list";
    }

    @PostMapping("/addBook")
    public String addBookToLibrary(Long libraryId, Long bookId, HttpServletRequest request) {
        libraryService.addBookToLibrary(libraryId, bookId);
        return "redirect:" + request.getHeader("referer");
    }
}
