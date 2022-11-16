package jarek.biblioteka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String titlePhrase;

    private int yearWritten;

    private String house;

    private String housePhrase;

    private SearchParameter searchParameter;

//    private List<String> searchParameterNamesList;

    private Long authorId;
    private String chosenAuthor = "NO";

    private Long libraryId;
    private String chosenLibrary = "NO";

    private Long houseId;
    private String chosenPublishingHouse = "NO";
}
