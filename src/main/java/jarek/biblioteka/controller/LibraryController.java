package jarek.biblioteka.controller;

import jarek.biblioteka.model.Library;
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

    @GetMapping("/books/{id}")
    public String listLibraryBooks(Model model,
                                   HttpServletRequest request,
                                   @PathVariable(name = "id") Long libraryId) {

        Optional<Library> optionalLibrary = libraryService.getLibrary(libraryId);
        if (optionalLibrary.isPresent()) {
            model.addAttribute("atr_listBooks", optionalLibrary.get().getBookList());

            return "library-books";
        }

        return "redirect:/library/list";
    }

    @PostMapping("/add")
    public String addLibrary(Library library) {

        libraryService.addLibrary(library);

        return "redirect:/library/list";
    }
}
