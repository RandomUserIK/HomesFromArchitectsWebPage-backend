package sk.hfa.projects.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
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
