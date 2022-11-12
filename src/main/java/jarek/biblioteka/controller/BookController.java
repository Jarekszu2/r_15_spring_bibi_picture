package jarek.biblioteka.controller;

import jarek.biblioteka.model.Author;
import jarek.biblioteka.model.Book;
import jarek.biblioteka.model.BookSearch;
import jarek.biblioteka.model.SearchParameter;
import jarek.biblioteka.model.dto.SearchBookRequest;
import jarek.biblioteka.repository.LibraryRepository;
import jarek.biblioteka.service.BookSearchService;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/book/")
public class BookController {

    private final BookService bookService;
    private final BookSearchService bookSearchService;
    private final PublishingHouseService publishingHouseService;
    private final LibraryRepository libraryRepository;

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

            Set<Author> authorSet = optionalBook.get().getAuthors();
            List<Author> authorList = new ArrayList<>(authorSet);
            List<Author> sortedAuthorList = authorList.stream()
                    .sorted(Comparator.comparing(Author::getSurname))
                    .collect(Collectors.toList());

            model.addAttribute("atr_authorList", sortedAuthorList);

            return "book-details";
        }
        return "redirect:/book/list";
    }

    @GetMapping("/status/{id}")
    public String setStatusAvailable(Model model,
                                     HttpServletRequest request,
                                     @PathVariable(name = "id") Long bookChangedStatusId) {

        Optional<Book> optionalBook = bookService.getById(bookChangedStatusId);
        if (optionalBook.isPresent()) {

//            optionalBook.get().setStatusLibrary(StatusLibrary.AVAILABLE);

            model.addAttribute("atr_book", optionalBook.get());
            model.addAttribute("atr_referer", request.getHeader("referer"));

            return "book-status";
        }
        return "redirect:/book/list";
    }

    @PostMapping("/status")
    public String setStatusAvailable(Book book, Long phId) {

        bookService.saveBook(book, phId);

        return "redirect:/book/list";
    }

    @GetMapping("/search")
    public String searchBook(Model model, BookSearch bookSearch, HttpServletRequest request) {

        SearchParameter[] parameters = SearchParameter.values();
        List<SearchParameter> searchParameterList = Arrays.asList(parameters);
        List<String> list = searchParameterList.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
        bookSearch.setTitle("");
//        bookSearch.setHouse("");
        model.addAttribute("atr_bookSearch", bookSearch);
        model.addAttribute("atr_referer", request.getHeader("referer"));
        model.addAttribute("atr_phList", publishingHouseService.getAll());
        model.addAttribute("atr_searchParameters", list);
        model.addAttribute("atr_libraryList", libraryRepository.findAll());


        return "book-search";
    }

    @PostMapping("/search")
    public String searchBook(BookSearch bookSearch,Long library_Id, Long p_HouseId, HttpServletRequest request) {
        bookSearchService.save(bookSearch, library_Id, p_HouseId, request);
        return "redirect:/book/searchList";
    }

    @GetMapping("/searchList")
    public String searchList(Model model, HttpServletRequest request) {
//        BookSearch bookSearch = bookSearchService.getBookSearch();
//        String title = bookSearch.getTitle();
//        int yearWritten = bookSearch.getYearWritten();
//        List<Book> bookList = bookService.getSearchListByYearWritten(yearWritten);
        SearchBookRequest searchBookRequest = bookService.getSearchBookDto();

        model.addAttribute("atr_name", searchBookRequest.getName());
        model.addAttribute("atr_value", searchBookRequest.getValue());
        model.addAttribute("atr_bookList", searchBookRequest.getBookList());
        model.addAttribute("atr_referer", request.getHeader("referer"));

        return "book-searchList";
    }

    @GetMapping(path = "/testList")
    public String testList(Model model) {
        List<Book> bookList = bookService.getAll();
        model.addAttribute("at_bookList", bookList);

        return "book-testList";
    }

    @GetMapping(path = "/detailsTest/{bookId}")
    public String detailsTest(@PathVariable(name = "bookId") Long bookDetailsId,
                              Model model,
                              HttpServletRequest request) {
        Optional<Book> optionalBook = bookService.getById(bookDetailsId);
        if (optionalBook.isPresent()) {
            model.addAttribute("at_book", optionalBook.get());
            model.addAttribute("at_referer", request.getHeader("referer"));

            return "book-detailsTest";
        }

        return "redirect:/book/listTest";
    }
}
