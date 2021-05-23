package sk.hfa.blog.services.interfaces;

import org.springframework.data.domain.Page;
import sk.hfa.blog.domain.BlogArticle;

public interface IBlogService {

    BlogArticle save(BlogArticle blogArticle);

    BlogArticle findById(Long id);

    void deleteById(Long id);

    Page<BlogArticle> getAllOnPage(int page, int size);

}
