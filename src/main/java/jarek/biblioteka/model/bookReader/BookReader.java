package jarek.biblioteka.model.bookReader;

import jarek.biblioteka.model.bookLent.BookLent;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookReader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private BookReaderSex bookReaderSex;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "bookReader", fetch = FetchType.LAZY)
    private BookLent bookLent;
}
