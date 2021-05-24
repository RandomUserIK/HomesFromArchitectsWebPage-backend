package sk.hfa.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Lob
    private byte[] opsAsByteArray;

    @Transient
    public List<DeltaOperation> getOps() {
        return (List<DeltaOperation>) SerializationUtils.deserialize(opsAsByteArray);
    }

    public void setOps(List<DeltaOperation> ops) {
        opsAsByteArray = SerializationUtils.serialize(ops);
    }

    public static BlogArticle build(BlogArticleDto blogArticleDto) {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setOps(blogArticleDto.getOps());
        return blogArticle;
    }

}
