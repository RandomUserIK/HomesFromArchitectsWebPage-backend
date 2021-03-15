package sk.hfa.auth.service.interfaces;

import sk.hfa.auth.domain.User;

import java.util.Optional;

public interface IUserService {

    Optional<User> findByUsername(String username);

}
