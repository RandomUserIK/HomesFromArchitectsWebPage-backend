package sk.hfa.blog.domain.throwable;

public class BlogArticleProcessingException extends RuntimeException {

    public BlogArticleProcessingException(String message) {
        super(message);
    }

}
