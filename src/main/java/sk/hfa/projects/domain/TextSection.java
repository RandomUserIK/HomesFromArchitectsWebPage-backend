package sk.hfa.projects.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class TextSection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Length(max = 100,
            message = "Title of text section must have max number of chars 100")
    private String title;

    @Length(max = 800,
            message = "Text of text section must have max number of chars 450")
    private String text;

}
