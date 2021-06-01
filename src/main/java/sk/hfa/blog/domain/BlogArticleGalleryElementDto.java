package sk.hfa.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticleGalleryElementDto {

    private Long id;

    private String title;

    private String titleImage;

    private String description;

    public static BlogArticleGalleryElementDto build(BlogArticle blogArticle) {
        return BlogArticleGalleryElementDto.builder()
                .id(blogArticle.getId())
                .title(blogArticle.getTitle())
                .titleImage(blogArticle.getTitleImage())
                .description(blogArticle.getDescription())
                .build();
    }

}
