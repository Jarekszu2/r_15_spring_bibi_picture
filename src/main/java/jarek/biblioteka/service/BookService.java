package jarek.biblioteka.service;

import jarek.biblioteka.exception.WrongOperation;
import jarek.biblioteka.model.Book;
import jarek.biblioteka.model.BookSearch;
import jarek.biblioteka.model.PublishingHouse;
import jarek.biblioteka.model.StatusLibrary;
import jarek.biblioteka.model.dto.SearchBookRequest;
import jarek.biblioteka.repository.BookRepository;
import jarek.biblioteka.repository.BookSearchRepository;
import jarek.biblioteka.repository.PublishingHouseRepository;
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
    private final PublishingHouseRepository publishingHouseRepository;
    private final BookSearchService bookSearchService;

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
            if (book.getAuthors() == null) {
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

        if (bookSearch.getSearchParameter().name().equals("HOUSE")) {

            searchBookRequest.setName("Saerch book list by publishing house: ");
            Optional<PublishingHouse> optionalPublishingHouse = publishingHouseRepository.findById(bookSearch.getHouseId());
            if (optionalPublishingHouse.isPresent()){
                PublishingHouse publishingHouse = optionalPublishingHouse.get();
                searchBookRequest.setValue(publishingHouse.getName());
                searchBookRequest.setBookList(bookRepository.findAllByPublishingHouse(publishingHouse));
                return searchBookRequest;
            }
        }

//        if (verifyNumber == 0){
//            searchBookRequest.setName("Search book: " + verifyNumber);
//            searchBookRequest.setValue("Something.");
//            searchBookRequest.setBookList(bookRepository.findAll());
//            return searchBookRequest;
//        }


        throw new WrongOperation("Wrong operation.");
    }

    public List<Book> getSearchListByYearWritten(int yearWritten) {
        return bookRepository.findAllByYearWritten(yearWritten);
    }
}
