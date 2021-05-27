package sk.hfa.blog.domain;

import lombok.*;
import org.springframework.util.SerializationUtils;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String titleImage;

    @Lob
    private byte[] opsAsByteArray;

    @Transient
    public List<DeltaOperation> getContent() {
        return (List<DeltaOperation>) SerializationUtils.deserialize(opsAsByteArray);
    }

    public void setOps(List<DeltaOperation> ops) {
        opsAsByteArray = SerializationUtils.serialize(ops);
    }

    public static BlogArticle build(BlogArticleDto blogArticleDto) {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setTitle(blogArticleDto.getTitle());
        blogArticle.setTitleImage(blogArticleDto.getTitleImage());
        blogArticle.setOps(blogArticleDto.getContent());
        return blogArticle;
    }

}
