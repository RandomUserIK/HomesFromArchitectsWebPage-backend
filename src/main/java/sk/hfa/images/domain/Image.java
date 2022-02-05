package sk.hfa.images.domain;

import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String extension;

    public static Image build(MultipartFile multipartFile) {
        return builder()
                .title(multipartFile.getOriginalFilename())
                .extension(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))
                .build();
    }

}
