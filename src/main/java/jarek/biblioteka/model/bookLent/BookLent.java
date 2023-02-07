package jarek.biblioteka.model.bookLent;

import jarek.biblioteka.model.Book;
import jarek.biblioteka.model.bookReader.BookReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookLent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookLentDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookReturnDate;

    private BookLentStatus bookLentStatus = BookLentStatus.ACTIVE;

    @OneToOne
    private Book book;

    @OneToOne
    private BookReader bookReader;

}
