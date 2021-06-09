package sk.hfa.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import sk.hfa.blog.domain.BlogArticle;
import sk.hfa.blog.services.interfaces.IBlogService;

@Slf4j
@Component
public class DummyBlogRunner implements CommandLineRunner {

    private final IBlogService blogService;

    @Value("classpath:title.png")
    private Resource titleImage;

    public DummyBlogRunner(IBlogService blogService) {
        this.blogService = blogService;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 30; i++) {
            BlogArticle blogArticle = new BlogArticle();
            blogArticle.setTitle("Blog " + i);
            blogArticle.setDescription("This is description of test blog");
            blogArticle.setTitleImage(titleImage.getFile().getPath());
            blogService.save(blogArticle);
        }
    }
}
