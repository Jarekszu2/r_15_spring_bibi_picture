package jarek.biblioteka.service;

import jarek.biblioteka.model.Library;
import jarek.biblioteka.repository.LibraryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    public List<Library> getAll() {
        return libraryRepository.findAll();
    }

    public void addLibrary(Library library) {
        libraryRepository.save(library);
    }

    public Optional<Library> getLibrary(Long libraryId) {
        return libraryRepository.findById(libraryId);
    }
}
