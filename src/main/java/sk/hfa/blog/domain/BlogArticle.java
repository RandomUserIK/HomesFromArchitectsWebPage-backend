package sk.hfa.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.SerializationUtils;
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

    public static BlogArticle build(BlogArticleDto blogArticleDto, Image titleImage) {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setId(blogArticleDto.getId());
        blogArticle.setTitle(blogArticleDto.getTitle());
        blogArticle.setTitleImage(titleImage);
        blogArticle.setDescription(blogArticleDto.getDescription());
        blogArticle.setOps(blogArticleDto.getContent());
        return blogArticle;
    }

}
