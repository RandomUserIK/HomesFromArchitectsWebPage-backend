package sk.hfa.instagram.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.hfa.instagram.domain.InstagramToken;

@Repository
public interface InstagramTokenRepository extends JpaRepository<InstagramToken,String> {
}
