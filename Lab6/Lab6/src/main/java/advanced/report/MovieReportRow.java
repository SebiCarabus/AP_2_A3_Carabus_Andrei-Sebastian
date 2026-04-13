package advanced.report;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@AllArgsConstructor
@Getter
public class MovieReportRow {
    private int id;
    private String title;
    private Date releaseDate;
    private int duration;
    private float score;
    private String genreName;
}