package sk.hfa.auth.service;

import org.springframework.stereotype.Service;
import sk.hfa.auth.domain.Role;
import sk.hfa.auth.domain.repositories.RoleRepository;
import sk.hfa.auth.service.interfaces.IRoleService;

import java.util.Objects;

@Service
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role save(Role role) {
        if (Objects.isNull(role))
            return null;

        return roleRepository.save(role);
    }

}
