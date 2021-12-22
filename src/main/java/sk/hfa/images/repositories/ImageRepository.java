package sk.hfa.images.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.hfa.images.domain.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
