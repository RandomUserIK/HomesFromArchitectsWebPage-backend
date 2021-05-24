package sk.hfa.blog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.hfa.blog.domain.BlogArticle;
import sk.hfa.blog.domain.BlogArticleDto;
import sk.hfa.blog.services.interfaces.IBlogService;
import sk.hfa.blog.web.domain.requestbodies.BlogArticleRequest;
import sk.hfa.blog.web.domain.responsebodies.BlogArticleMessageResource;
import sk.hfa.blog.web.domain.responsebodies.BlogArticlePageMessageResource;
import sk.hfa.web.domain.responsebodies.DeleteEntityMessageResource;
import sk.hfa.web.domain.responsebodies.MessageResource;

@Slf4j
@RestController
@RequestMapping(path = "/api/blog")
public class BlogController {

    private final IBlogService blogService;

    public BlogController(IBlogService blogService) {
        this.blogService = blogService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> createBlogArticle(@RequestBody BlogArticleRequest request) {
        log.info("Creating a new blog article.");
        BlogArticle blogArticle = blogService.save(BlogArticle.build(request.getBlogArticle()));
        MessageResource responseBody = new BlogArticleMessageResource(BlogArticleDto.build(blogArticle));
        log.info("The blog article with the ID: [" + blogArticle.getId() + "] was successfully created.");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> getBlogArticle(@PathVariable long id) {
        log.info("Fetching the blog article with the ID: " + id);
        BlogArticle blogArticle = blogService.findById(id);
        MessageResource responseBody = new BlogArticleMessageResource(BlogArticleDto.build(blogArticle));
        return ResponseEntity.ok(responseBody);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> deleteBlogArticle(@PathVariable long id) {
        log.info("Deleting the blog article with the ID: [" + id + "]");
        blogService.deleteById(id);
        MessageResource responseBody = new DeleteEntityMessageResource("Blog article was successfully deleted");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResource> getAllOnPage(@RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        log.info("Fetching blog articles on the page [" + page + "]");
        Page<BlogArticle> result = blogService.getAllOnPage(page, size);
        MessageResource responseBody = BlogArticlePageMessageResource.build(result);
        return ResponseEntity.ok(responseBody);
    }

}
