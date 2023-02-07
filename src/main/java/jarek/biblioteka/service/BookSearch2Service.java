package jarek.biblioteka.service;

import jarek.biblioteka.model.BookSearch2;
import jarek.biblioteka.repository.BookSearch2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookSearch2Service {

    @Autowired
    private BookSearch2Repository bookSearch2Repository;

//    public BookSearch2 getBookSearch2() {
//        return bookSearch2Repository.getAll().get(0);
//    }
}
