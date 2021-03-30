package sk.hfa.projects.web.domain.responsebodies;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ImageUploadMessageResource implements MessageResource {

    private String filePath;

}
