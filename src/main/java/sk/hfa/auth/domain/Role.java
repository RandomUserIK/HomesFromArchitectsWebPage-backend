package sk.hfa.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @NotNull
    @Column(unique = true)
    @Getter
    @Setter
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Set<User> users;

    public Role(@NotNull String name) {
        this.name = name;
        this.users = new LinkedHashSet<>();
    }

}
