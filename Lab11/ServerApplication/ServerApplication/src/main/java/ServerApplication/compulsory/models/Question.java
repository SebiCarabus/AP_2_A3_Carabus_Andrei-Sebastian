package ServerApplication.compulsory.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="text")
    private String text;

    @Column(name="option1")
    private String option1;

    @Column(name="option2")
    private String option2;

    @Column(name="option3")
    private String option3;

    @Column(name="option4")
    private String option4;

    @Column(name="correct_option_index")
    private Integer correctOptionIndex;

    @Transient
    private List<String> options;

    @PostLoad
    private void onLoad() {
        this.options = Arrays.asList(option1, option2, option3, option4);
    }
}
