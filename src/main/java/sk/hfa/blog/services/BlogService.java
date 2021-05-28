package sk.hfa.blog.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sk.hfa.blog.domain.BlogArticle;
import sk.hfa.blog.domain.repositories.BlogArticleRepository;
import sk.hfa.blog.domain.throwable.BlogArticleNotFoundException;
import sk.hfa.blog.services.interfaces.IBlogService;
import sk.hfa.images.services.ImageService;
import sk.hfa.projects.domain.throwable.InvalidPageableRequestException;
import sk.hfa.util.Constants;

import java.util.Objects;

@Slf4j
@Service
public class BlogService implements IBlogService {

    private final ImageService imageService;
    private final BlogArticleRepository blogArticleRepository;

    public BlogService(ImageService imageService, BlogArticleRepository blogArticleRepository) {
        this.imageService = imageService;
        this.blogArticleRepository = blogArticleRepository;
    }

    @Override
    public BlogArticle save(BlogArticle blogArticle) {
        if (Objects.isNull(blogArticle))
            throw new IllegalArgumentException("Invalid blog article provided");

        return blogArticleRepository.save(blogArticle);
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

        imageService.deleteBlogArticleImage(id);
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

}
