package jarek.biblioteka.service;

import jarek.biblioteka.model.Author;
import jarek.biblioteka.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    public Page<Author> getPage(PageRequest of) {
        return authorRepository.findAll(of);
    }
}
