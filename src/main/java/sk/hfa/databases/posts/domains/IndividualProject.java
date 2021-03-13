package sk.hfa.databases.posts.domains;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class IndividualProject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    @ElementCollection
    private List<String> photoPaths = new ArrayList<String>();
}

