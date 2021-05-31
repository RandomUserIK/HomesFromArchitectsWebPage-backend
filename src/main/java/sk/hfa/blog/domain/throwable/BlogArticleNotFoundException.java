package sk.hfa.blog.domain.throwable;

public class BlogArticleNotFoundException extends RuntimeException {

    public BlogArticleNotFoundException(String message) {
        super(message);
    }

}
