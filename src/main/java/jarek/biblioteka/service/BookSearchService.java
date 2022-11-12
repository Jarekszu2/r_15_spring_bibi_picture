package jarek.biblioteka.service;

import jarek.biblioteka.model.BookSearch;
import jarek.biblioteka.model.SearchParameter;
import jarek.biblioteka.repository.BookSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class BookSearchService {

    @Autowired
    private BookSearchRepository bookSearchRepository;

    public void save(BookSearch bookSearch, Long library_Id, Long p_houseId, HttpServletRequest request) {
        bookSearchRepository.deleteAll();
//        if (bookSearch.getTitle().equals("") && bookSearch.getYearWritten() == 0 && bookSearch.getHouse().equals("")){
//            bookSearch.setHouse(p_HouseName);
//        }

        Map<String, String[]> formParameters = request.getParameterMap();
//        int verifyNumber = 0;
//        String param = ""
        Set<String> searchParameters = formParameters.keySet();
        for (String parameter : searchParameters){
            String[] values = formParameters.get(parameter);
            if (values[0].equals("on")){
//                param = parameter;
                SearchParameter searchParameter = SearchParameter.valueOf(parameter);
                bookSearch.setSearchParameter(searchParameter);
            }
        }

        bookSearch.setLibraryId(library_Id);
        bookSearch.setHouseId(p_houseId);
        bookSearchRepository.save(bookSearch);
    }

    BookSearch getBookSearch() {
        List<BookSearch> bookSearchList = bookSearchRepository.findAll();
        return bookSearchList.get(0);
    }
}
