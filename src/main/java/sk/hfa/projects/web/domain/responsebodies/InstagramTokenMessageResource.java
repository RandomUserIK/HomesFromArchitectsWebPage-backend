package sk.hfa.projects.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.util.List;

@Data
@AllArgsConstructor
public class InstagramTokenMessageResource implements MessageResource{
    private List<InstagramBodyValueResponse> instagramData;
}
