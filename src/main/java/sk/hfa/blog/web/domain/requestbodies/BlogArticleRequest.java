package sk.hfa.blog.web.domain.requestbodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.hfa.blog.domain.BlogArticle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticleRequest {

    private BlogArticle blogArticle;

}
