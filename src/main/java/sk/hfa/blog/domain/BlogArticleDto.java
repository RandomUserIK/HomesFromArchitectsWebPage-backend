package sk.hfa.blog.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticleDto {

    private List<DeltaOperation> content;

    private String title;

    private String titleImage;

    public static BlogArticleDto build(BlogArticle blogArticle) {
        return BlogArticleDto.builder()
                .title(blogArticle.getTitle())
                .titleImage(blogArticle.getTitleImage())
                .content(blogArticle.getContent())
                .build();
    }

}
