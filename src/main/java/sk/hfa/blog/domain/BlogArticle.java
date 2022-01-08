package sk.hfa.blog.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.SerializationUtils;
import sk.hfa.blog.domain.throwable.BlogArticleProcessingException;
import sk.hfa.images.domain.Image;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotBlank
    @Length(max = 50, message = "Blog article title can have at most 50 letters")
    private String title;

    @Getter
    @Setter
    @OneToOne
    private Image titleImage;

    @Getter
    @Setter
    @NotBlank
    @Length(max = 200, message = "Blog article description can have at most 200 letters")
    private String description;

    @Lob
    private byte[] opsAsByteArray;

    @Transient
    public List<DeltaOperation> getContent() {
        return (List<DeltaOperation>) SerializationUtils.deserialize(opsAsByteArray);
    }

    public void setOps(List<DeltaOperation> ops) {
        opsAsByteArray = SerializationUtils.serialize(ops);
    }

    public static BlogArticle build(BlogArticleDto blogArticleDto, Image titleImage) throws JsonProcessingException {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setId(blogArticleDto.getId());
        blogArticle.setTitle(blogArticleDto.getTitle());
        blogArticle.setTitleImage(titleImage);
        blogArticle.setDescription(blogArticleDto.getDescription());
        try {
            blogArticle.setOps(new ObjectMapper().readValue(blogArticleDto.getContent(), List.class));
        } catch (JsonProcessingException | NullPointerException ex) {
            throw new BlogArticleProcessingException(ex.getMessage());
        }
        return blogArticle;
    }

}
