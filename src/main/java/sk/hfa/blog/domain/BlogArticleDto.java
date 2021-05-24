package sk.hfa.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticleDto {

    private List<DeltaOperation> ops;

    public static BlogArticleDto build(BlogArticle blogArticle) {
        return BlogArticleDto.builder()
                .ops(blogArticle.getOps())
                .build();
    }

}
