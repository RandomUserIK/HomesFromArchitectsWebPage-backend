package sk.hfa.projects.web.domain.responsebodies;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class ImageMessageResource implements MessageResource {

    private InputStream inputStream;

}
