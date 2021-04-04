package sk.hfa.projects.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TextSection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String text;

}
