package sk.hfa.auth.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.hfa.auth.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
