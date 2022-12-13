package jarek.biblioteka.model;

import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Formula(value = "(select count(*) from author_books where author_books.authors_id = id)")
    private Integer booksWritten;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Book> books;

    @OneToOne
    private AuthorPhoto photo;

    public String convertBinImageToString(){
        if (photo != null && photo.getFoto().length > 0) {
            return Base64.getEncoder().encodeToString(photo.getFoto());
        }
        return "";
    }
}
