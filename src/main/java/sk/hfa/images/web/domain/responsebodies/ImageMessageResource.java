package sk.hfa.images.web.domain.responsebodies;

import lombok.Data;
import lombok.AllArgsConstructor;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class ImageMessageResource implements MessageResource {

    private InputStream inputStream;

}
