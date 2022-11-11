package jarek.biblioteka.model.dto;

import jarek.biblioteka.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookRequest {

    private List<Book> bookList;

    private String name;

    private String value;
}
