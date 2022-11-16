package jarek.biblioteka.service;

import jarek.biblioteka.model.BookSearch;
import jarek.biblioteka.model.BookSearch2;
import jarek.biblioteka.model.SearchParameter;
import jarek.biblioteka.repository.BookSearch2Repository;
import jarek.biblioteka.repository.BookSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class BookSearchService {

    @Autowired
    private BookSearchRepository bookSearchRepository;

    @Autowired
    private BookSearch2Repository bookSearch2Repository;

    public void save(BookSearch bookSearch, Long author_Id, Long library_Id, Long p_houseId, HttpServletRequest request) {
        bookSearchRepository.deleteAll();
        bookSearch2Repository.deleteAll();

        Map<String, String[]> formParameters = request.getParameterMap();
        List<SearchParameter> searchParameterList = new ArrayList<>();
        Set<String> stringSet = formParameters.keySet();
        for (String parameter : stringSet){
            String[] values = formParameters.get(parameter);
            if (values[0].equals("on")){
                SearchParameter searchParameter = SearchParameter.valueOf(parameter);
                searchParameterList.add(searchParameter);

                if (searchParameterList.contains(SearchParameter.AUTHOR)){
                    bookSearch.setChosenAuthor("YES");
                }

                if (searchParameterList.contains(SearchParameter.LIBRARY)){
                    bookSearch.setChosenLibrary("YES");
                }

                if (searchParameterList.contains(SearchParameter.HOUSE)){
                    bookSearch.setChosenPublishingHouse("YES");
                }

//                BookSearch2 bookSearch2 = new BookSearch2();
//                bookSearch2.setSearchParameterList(searchParameterList);
//                bookSearch2Repository.save(bookSearch2);
            }
        }

//        bookSearch.setSearchParameterList(searchParameterList);
        bookSearch.setAuthorId(author_Id);
        bookSearch.setLibraryId(library_Id);
        bookSearch.setHouseId(p_houseId);
        bookSearchRepository.save(bookSearch);

    }

    public void save(BookSearch bookSearch){
        bookSearchRepository.save(bookSearch);
    }

    BookSearch getBookSearch() {
        List<BookSearch> bookSearchList = bookSearchRepository.findAll();
        return bookSearchList.get(0);
    }
}
