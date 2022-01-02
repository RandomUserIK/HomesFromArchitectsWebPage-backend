package sk.hfa.blog.web.domain.responsebodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import sk.hfa.blog.domain.BlogArticle;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Data
@AllArgsConstructor
public class BlogArticleMessageResource implements MessageResource {

    private BlogArticle blogArticle;

}
