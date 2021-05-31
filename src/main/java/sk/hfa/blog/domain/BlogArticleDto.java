package sk.hfa.blog.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticleDto {

    private Long id;

    private String title;

    private String titleImage;

    private String description;

    private List<DeltaOperation> content;

    public static BlogArticleDto build(BlogArticle blogArticle) {
        return BlogArticleDto.builder()
                .id(blogArticle.getId())
                .title(blogArticle.getTitle())
                .titleImage(blogArticle.getTitleImage())
                .description(blogArticle.getDescription())
                .content(blogArticle.getContent())
                .build();
    }

}
