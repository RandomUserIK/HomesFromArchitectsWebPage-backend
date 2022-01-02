package sk.hfa.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticleDto {

    private Long id;

    private String title;

    private MultipartFile titleImage;

    private String description;

    private List<DeltaOperation> content;

}
