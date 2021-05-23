package sk.hfa.instagram.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstagramToken {

    @Id
    private String instagramToken;
    private boolean refreshSuccessful;
}
