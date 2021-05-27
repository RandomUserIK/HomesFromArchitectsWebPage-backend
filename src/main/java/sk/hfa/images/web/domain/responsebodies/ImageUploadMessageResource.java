package sk.hfa.images.web.domain.responsebodies;

import lombok.Data;
import lombok.AllArgsConstructor;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Data
@AllArgsConstructor
public class ImageUploadMessageResource implements MessageResource {

    private String filePath;

}
