package sk.hfa.blog.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.hfa.blog.domain.BlogArticle;
import sk.hfa.blog.domain.BlogArticleDto;
import sk.hfa.blog.domain.repositories.BlogArticleRepository;
import sk.hfa.blog.domain.throwable.BlogArticleNotFoundException;
import sk.hfa.blog.services.interfaces.IBlogService;
import sk.hfa.images.domain.Image;
import sk.hfa.images.services.interfaces.IImageService;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.util.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BlogService implements IBlogService {

    private final IImageService imageService;
    private final BlogArticleRepository blogArticleRepository;

    public BlogService(IImageService imageService, BlogArticleRepository blogArticleRepository) {
        this.imageService = imageService;
        this.blogArticleRepository = blogArticleRepository;
    }

    @Override
    public BlogArticle save(BlogArticleDto blogArticleDto) throws JsonProcessingException {
        if (Objects.isNull(blogArticleDto))
            throw new IllegalArgumentException("Invalid blog article provided");

        Image titleImage = imageService.save(blogArticleDto.getTitleImage());
        return blogArticleRepository.save(BlogArticle.build(blogArticleDto, titleImage));
    }

    @Override
    public BlogArticle findById(Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE);

        return blogArticleRepository.findById(id).orElseThrow(() ->
                new BlogArticleNotFoundException("Blog article not found for the given ID: [" + id + "]"));
    }

    @Override
    public void deleteById(Long id) {
        if (Objects.isNull(id))
            throw new IllegalArgumentException(Constants.INVALID_IDENTIFIER_MESSAGE);

        BlogArticle blogArticle = findById(id);
        imageService.deleteImage(blogArticle.getTitleImage());
        blogArticleRepository.deleteById(id);
    }

    @Override
    public Page<BlogArticle> getAllOnPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BlogArticle> result = blogArticleRepository.findAll(pageRequest);

        if (page > result.getTotalPages())
            throw new InvalidPageableRequestException(Constants.INVALID_PAGEABLE_MESSAGE);

        return result;
    }

    @Override
    public Page<BlogArticle> getRandomBlogs(int size) {
        List<BlogArticle> articles = blogArticleRepository.findAll();
        if (size >= articles.size())
            return this.prepareRandomBlogs(articles, size);
        return this.prepareRandomBlogs(getRandomArticles(articles, size), size);
    }

    private Page<BlogArticle> prepareRandomBlogs(List<BlogArticle> articleList, int size) {
        return new PageImpl<>(articleList,
                PageRequest.of(0, size, Sort.by("id")),
                articleList.size());
    }

    private List<BlogArticle> getRandomArticles(List<BlogArticle> articles, int size) {
        Collections.shuffle(articles);
        return articles.subList(0, size);
    }
}
