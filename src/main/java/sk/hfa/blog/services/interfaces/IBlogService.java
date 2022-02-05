package sk.hfa.blog.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import sk.hfa.blog.domain.BlogArticle;
import sk.hfa.blog.domain.BlogArticleDto;

public interface IBlogService {

    BlogArticle save(BlogArticleDto blogArticleDto) throws JsonProcessingException;

    BlogArticle findById(Long id);

    void deleteById(Long id);

    Page<BlogArticle> getAllOnPage(int page, int size);

    Page<BlogArticle> getRandomBlogs(int size);

}
