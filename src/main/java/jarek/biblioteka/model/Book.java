package jarek.biblioteka.model;

import jarek.biblioteka.model.bookLent.BookLent;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private int yearWritten;

    @Formula(value = "(year(now()) - year_written)")
    private int howOld;

    private int numberOfPages;

    private int numberOfAvailableCopies;

    private StatusLibrary statusLibrary;

    @ManyToOne(fetch = FetchType.LAZY)
    private PublishingHouse publishingHouse;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER)
    private Set<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY)
    private Library library;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "book", fetch = FetchType.LAZY)
    private BookLent bookLent;

    public Book(String title, int yearWritten, int numberOfPages, int numberOfAvailableCopies) {
        this.title = title;
        this.yearWritten = yearWritten;
        this.numberOfPages = numberOfPages;
        this.numberOfAvailableCopies = numberOfAvailableCopies;
    }
}
