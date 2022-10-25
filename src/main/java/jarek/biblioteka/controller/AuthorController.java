package jarek.biblioteka.controller;

import jarek.biblioteka.model.Author;
import jarek.biblioteka.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/author/")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

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
}
