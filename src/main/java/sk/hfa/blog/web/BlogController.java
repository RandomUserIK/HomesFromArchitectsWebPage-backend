package sk.hfa.blog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.hfa.blog.services.interfaces.IBlogService;

@Slf4j
@RestController
@RequestMapping(path = "/api/blog")
public class BlogController {

    private final IBlogService blogService;

    public BlogController(IBlogService blogService) {
        this.blogService = blogService;
    }

}
