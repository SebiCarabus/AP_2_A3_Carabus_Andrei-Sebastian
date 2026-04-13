package advanced.importer;

import homework.objects.Movie;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MovieList {
    private int id;
    private String name;
    private Timestamp createdAt;
    private List<Movie> movies = new ArrayList<>();

    public MovieList(String name) {
        this.name = name;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}