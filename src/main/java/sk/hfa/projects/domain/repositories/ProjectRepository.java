package sk.hfa.projects.domain.repositories;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import sk.hfa.projects.domain.Project;
import sk.hfa.projects.domain.QCommonProject;
import sk.hfa.projects.domain.QProject;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project>, QuerydslBinderCustomizer<QProject> {

    Optional<Project> findById(Long id);

    Page<Project> findAll(Pageable pageable);

    Page<Project> findAll(Predicate predicate, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QProject qProject) {
        bindings.bind(qProject.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(qProject.category).first(SimpleExpression::eq);
        bindings.bind(qProject.hasGarage).first(SimpleExpression::eq);
        bindings.bind((qProject.as(QCommonProject.class)).hasStorey).first(StringExpression::eq);
        bindings.bind((qProject.as(QCommonProject.class)).rooms).first((path, value) ->
                (value <= 5) ? path.eq(value) : path.goe(value)
        );
    }

}
