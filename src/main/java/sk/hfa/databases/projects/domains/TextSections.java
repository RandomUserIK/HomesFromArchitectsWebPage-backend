package sk.hfa.databases.projects.domains;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TextSections {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String text;

}
