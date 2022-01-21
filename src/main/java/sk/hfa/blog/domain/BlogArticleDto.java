package sk.hfa.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticleDto {

    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private MultipartFile titleImage;

    @NotBlank
    private String description;

    @NotBlank
    private String content;

}
