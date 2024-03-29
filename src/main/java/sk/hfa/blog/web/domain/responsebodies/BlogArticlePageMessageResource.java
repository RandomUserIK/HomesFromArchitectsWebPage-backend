package sk.hfa.blog.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import sk.hfa.blog.domain.BlogArticle;
import sk.hfa.util.Constants;
import sk.hfa.web.domain.responsebodies.MessageResource;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class BlogArticlePageMessageResource implements MessageResource {

    private int currentPage;

    private long totalElements;

    private int elementsPerPage;

    private List<BlogArticle> blogArticles;

    public static BlogArticlePageMessageResource build(Page<BlogArticle> page, boolean isGalleryPreview) {
        if (Objects.isNull(page))
            throw new IllegalArgumentException(Constants.INVALID_PAGE_MESSAGE);

        if (!isGalleryPreview)
            page.getContent().forEach(blogArticle -> blogArticle.getContent().clear());

        return BlogArticlePageMessageResource.builder()
                .currentPage(page.getNumber())
                .blogArticles(page.getContent())
                .totalElements(page.getTotalElements())
                .elementsPerPage(Constants.ELEMENTS_PER_PAGE)
                .build();
    }
}
