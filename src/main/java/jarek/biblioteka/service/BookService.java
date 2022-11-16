package jarek.biblioteka.service;

import jarek.biblioteka.exception.WrongOperation;
import jarek.biblioteka.model.*;
import jarek.biblioteka.model.dto.SearchBookRequest;
import jarek.biblioteka.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookSearchService bookSearchService;
    private final BookSearch2Service bookSearch2Service;
    private final AuthorRepository authorRepository;
    private final LibraryRepository libraryRepository;
    private final PublishingHouseRepository publishingHouseRepository;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public void save(Book book, Long pub_houseId) {
        if (publishingHouseRepository.existsById(pub_houseId)) {
            PublishingHouse publishingHouse = publishingHouseRepository.getById(pub_houseId);
            book.setPublishingHouse(publishingHouse);
            book.setStatusLibrary(StatusLibrary.AVAILABLE);
            bookRepository.save(book);
        } else {
            throw new EntityNotFoundException("Publishing house not found.");
        }
    }

    public Optional<Book> getById(Long editedBookId) {
        return bookRepository.findById(editedBookId);
    }

    public void removeById(Long deleted_id) {
//        bookRepository.deleteById(deleted_id);

        Optional<Book> optionalBook = bookRepository.findById(deleted_id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.getAuthors().size() == 0) {
                bookRepository.delete(book);
            } else {
                throw new WrongOperation("Can not remove book with assigned authors!");
            }
        }
    }


    public void saveBook(Book book, Long phId) {
        Optional<PublishingHouse> optionalPublishingHouse = publishingHouseRepository.findById(phId);
        if (optionalPublishingHouse.isPresent()) {

            PublishingHouse publishingHouse = optionalPublishingHouse.get();
            book.setPublishingHouse(publishingHouse);
            book.setStatusLibrary(StatusLibrary.AVAILABLE);
            bookRepository.save(book);
        }
    }

    public List<Book> getSearchByTitle(String title) {
        return bookRepository.findAllByTitle(title);
    }

    public SearchBookRequest getSearchBookDto(){

        SearchBookRequest searchBookRequest = new SearchBookRequest();
        BookSearch bookSearch = bookSearchService.getBookSearch();
//        BookSearch2 bookSearch2 = bookSearch2Service.getBookSearch2();
//        List<SearchParameter> searchParameterList = bookSearch2.getSearchParameterList();

//        Map<String, String[]> formParameters = request.getParameterMap();
//        int verifyNumber = 0;
//        Set<String> pHouseNames = formParameters.keySet();
//        for (String pHouseName : pHouseNames){
//            String[] values = formParameters.get(pHouseName);
//            if (values[0].equals("on")){
//                verifyNumber++;
//            }
//        }

        if (!bookSearch.getTitle().equals("")){

            searchBookRequest.setName("Search book list by title: ");
            searchBookRequest.setValue(bookSearch.getTitle());
            searchBookRequest.setBookList(bookRepository.findAllByTitle(bookSearch.getTitle()));
//            String title = bookSearch.getTitle();
//            return bookRepository.findAllByTitle(title);
            return searchBookRequest;
        }

        if (!bookSearch.getTitlePhrase().equals("")){

            searchBookRequest.setName("Search book list by title phrase: ");

            String titlePhrase = bookSearch.getTitlePhrase();
            searchBookRequest.setValue(titlePhrase);

            List<String> titleList = bookRepository.findAll().stream()
                    .map(Book::getTitle).collect(Collectors.toList());
            List<String> titleContainsPhraseList = titleList.stream()
                    .filter(s -> s.contains(titlePhrase))
                    .collect(Collectors.toList());
            if (titleContainsPhraseList.size() != 0) {
                List<Book> searchList = new ArrayList<>();
                for (String title : titleContainsPhraseList){
                    searchList.addAll(bookRepository.findAllByTitle(title));
                }
//                for (int i = 0; i < titleContainsPhraseList.size(); i++) {
//                    searchList.addAll(bookRepository.findAllByTitle(titleContainsPhraseList.get(i)));
//                }
                searchBookRequest.setBookList(searchList);
                return searchBookRequest;
            }
        }

        if(bookSearch.getYearWritten() != 0){

            searchBookRequest.setName("Search book list by year written: ");
            searchBookRequest.setValue(String.valueOf(bookSearch.getYearWritten()));
            searchBookRequest.setBookList(bookRepository.findAllByYearWritten(bookSearch.getYearWritten()));
//            int yearWritten = bookSearch.getYearWritten();
//            return bookRepository.findAllByYearWritten(yearWritten);
            return searchBookRequest;
        }

        if (!bookSearch.getHouse().equals("")){

            searchBookRequest.setName("Saerch book list by publishing house: ");
            String house = bookSearch.getHouse();
            searchBookRequest.setValue(house);
            List<PublishingHouse> publishingHouseList = publishingHouseRepository.findAll();
            Optional<PublishingHouse> optionalPublishingHouse = publishingHouseList.stream()
                    .filter(publishingHouse -> publishingHouse.getName().equals(house)).findAny();
            if(optionalPublishingHouse.isPresent()){
                PublishingHouse publishingHouse = optionalPublishingHouse.get();
                searchBookRequest.setBookList(bookRepository.findAllByPublishingHouse(publishingHouse));
                return searchBookRequest;
            }
        }

        if (!bookSearch.getHousePhrase().equals("")){

            searchBookRequest.setName("Saerch book list by publishing house phrase: ");
            String phrase = bookSearch.getHousePhrase();
            searchBookRequest.setValue(phrase);
            List<PublishingHouse> publishingHouseList = publishingHouseRepository.findAll();
            Optional<PublishingHouse> optionalPublishingHouse = publishingHouseList.stream()
                    .filter(publishingHouse -> publishingHouse.getName().contains(phrase)).findAny();
            if(optionalPublishingHouse.isPresent()){
                PublishingHouse publishingHouse = optionalPublishingHouse.get();
                searchBookRequest.setBookList(bookRepository.findAllByPublishingHouse(publishingHouse));
                return searchBookRequest;
            }
        }

        if (bookSearch.getChosenAuthor().equals("YES") && bookSearch.getChosenLibrary().equals("NO") && bookSearch.getChosenPublishingHouse().equals("NO")){
            searchBookRequest.setName("Search book list by author: ");
            Optional<Author> optionalAuthor = authorRepository.findById(bookSearch.getAuthorId());
            if (optionalAuthor.isPresent()){
                Author author = optionalAuthor.get();
                searchBookRequest.setValue(author.getName() + " " + author.getSurname());
                searchBookRequest.setBookList(bookRepository.findAllByAuthors(author));
                return searchBookRequest;
            }
        }

        if (bookSearch.getChosenAuthor().equals("NO") && bookSearch.getChosenLibrary().equals("YES") && bookSearch.getChosenPublishingHouse().equals("NO")) {

            searchBookRequest.setName("Search book list by library: ");
            Optional<Library> optionalLibrary = libraryRepository.findById(bookSearch.getLibraryId());
            if (optionalLibrary.isPresent()){
                Library library = optionalLibrary.get();
                searchBookRequest.setValue(library.getCity() + " " + library.getAddress());
                searchBookRequest.setBookList(bookRepository.findAllByLibrary(library));
                return searchBookRequest;
            }
        }

        if (bookSearch.getChosenAuthor().equals("NO") && bookSearch.getChosenLibrary().equals("NO") && bookSearch.getChosenPublishingHouse().equals("YES")) {

            searchBookRequest.setName("Search book list by publishing house: ");
            Optional<PublishingHouse> optionalPublishingHouse = publishingHouseRepository.findById(bookSearch.getHouseId());
            if (optionalPublishingHouse.isPresent()){
                PublishingHouse publishingHouse = optionalPublishingHouse.get();
                searchBookRequest.setValue(publishingHouse.getName());
                searchBookRequest.setBookList(bookRepository.findAllByPublishingHouse(publishingHouse));
                return searchBookRequest;
            }
        }

        if (bookSearch.getChosenAuthor().equals("YES") && bookSearch.getChosenLibrary().equals("YES") && bookSearch.getChosenPublishingHouse().equals("NO")){

            searchBookRequest.setName("Search book list by author and library:");

            Author author = null;
            Library library = null;

            Optional<Author> optionalAuthor = authorRepository.findById(bookSearch.getAuthorId());
            if (optionalAuthor.isPresent()){
                author = optionalAuthor.get();
            }

            Optional<Library> optionalLibrary = libraryRepository.findById(bookSearch.getLibraryId());
            if (optionalLibrary.isPresent()){
                library = optionalLibrary.get();
            }

            if (author != null && library != null) {
                searchBookRequest.setValue(author.getSurname() + " and " + library.getAddress());
//                searchBookRequest.setBookList(bookRepository.findAllByAuthors(author));
//                searchBookRequest.setBookList(bookRepository.findAllByAuthorsAndLibrary(searchAuthor, library));
//                Library finalLibrary = library;
//                searchBookRequest.setBookList(bookRepository.findAllByAuthors(author).stream()
//                            .filter(book -> book.getLibrary().equals(finalLibrary))
//                            .collect(Collectors.toList()));
//                Author finalAuthor = author;
                List<Book> byAuthor = bookRepository.findAllByAuthors(author);
                List<Book> byLibrary = bookRepository.findAllByLibrary(library);
                List<Book> byAuthorAndLibrary = new ArrayList<>();
                for (int i = 0; i < byAuthor.size(); i++) {
                    for (int j = 0; j < byLibrary.size(); j++) {
                        if (byAuthor.get(i).equals(byLibrary.get(j))) {
                            byAuthorAndLibrary.add(byAuthor.get(i));
                        }
                    }
                }
                searchBookRequest.setBookList(byAuthorAndLibrary);


//                bookSearch.setTitle(author.getSurname());
//                bookSearch.setTitlePhrase(String.valueOf(byAuthor.size()));
//                bookSearch.setHouse(library.getAddress());
//                bookSearch.setHousePhrase(String.valueOf(byLibrary.size()));
//                bookSearch.setYearWritten(listByAuthor.size());
                bookSearchService.save(bookSearch);

                return searchBookRequest;
            }

        }

        throw new WrongOperation("Wrong operation.");
    }

    public List<Book> getSearchListByYearWritten(int yearWritten) {
        return bookRepository.findAllByYearWritten(yearWritten);
    }

    public List<Book> getAllAvailable() {
        return bookRepository.findAllByStatusLibrary(StatusLibrary.AVAILABLE);
    }
}
