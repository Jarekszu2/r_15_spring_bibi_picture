package jarek.biblioteka.repository;

import jarek.biblioteka.model.AuthorPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorPhotoRepository extends JpaRepository<AuthorPhoto, Long> {
}
