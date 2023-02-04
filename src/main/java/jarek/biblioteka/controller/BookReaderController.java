package jarek.biblioteka.controller;

import jarek.biblioteka.model.bookReader.BookReader;
import jarek.biblioteka.model.bookReader.BookReaderSex;
import jarek.biblioteka.service.BookReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/bookReader/")
public class BookReaderController {

    @Autowired
    private BookReaderService bookReaderService;

    @GetMapping(path = "/add")
    public String addBookReader(Model model, BookReader bookReader) {
        model.addAttribute("atr_bookReader", bookReader);
        model.addAttribute("atr_tabBookReaderSex", BookReaderSex.values());

        return "bookReader-add";
    }

    @PostMapping(path = "/add")
    public String postAddBookReader(BookReader bookReader, String bookReaderSex) {
        bookReaderService.addBookReader(bookReader, bookReaderSex);

        return "redirect:/bookReader/list";
    }

    @GetMapping(path = "/list")
    public String listBookReaders(Model model,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "5") int size) {
        Page<BookReader> bookReaderPage = bookReaderService.getPage(PageRequest.of(page, size));

        model.addAttribute("atr_BookReaderList", bookReaderPage);

        return "bookReader-list";
    }

    @GetMapping("/remove/{id}")
    public String removeBookReader(@PathVariable(name = "id") Long removed_Id) {
        bookReaderService.deleteBookReader(removed_Id);
        return "redirect:/bookReader/list";
    }

    @GetMapping(path = "/edit/{id}")
    public String editBookReader(Model model,
                                 @PathVariable(name = "id") Long edited_Id) {
        Optional<BookReader> optionalBookReader = bookReaderService.findByID(edited_Id);
        if (optionalBookReader.isPresent()) {
            model.addAttribute("atr_bookReader", optionalBookReader.get());
            model.addAttribute("atr_tabBookReaderSex", BookReaderSex.values());

            return "bookReader-add";
        }

        return "redirect:/bookReader/list";
    }
}
