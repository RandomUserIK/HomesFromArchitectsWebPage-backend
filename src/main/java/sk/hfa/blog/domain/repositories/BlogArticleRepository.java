package sk.hfa.blog.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.hfa.blog.domain.BlogArticle;

import java.util.Optional;

@Repository
public interface BlogArticleRepository extends JpaRepository<BlogArticle, Long> {

    Optional<BlogArticle> findById(Long id);

    Page<BlogArticle> findAll(Pageable pageable);

}
