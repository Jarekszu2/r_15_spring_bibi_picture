package jarek.biblioteka.repository;

import jarek.biblioteka.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
