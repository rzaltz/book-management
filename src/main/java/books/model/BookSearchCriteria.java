package books.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookSearchCriteria {
    private List<String> titles;
    private List<String> authors;
    private List<String> tags;
    private List<String> isbns;
}
