package sk.hfa.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.hfa.auth.domain.User;
import sk.hfa.auth.domain.repositories.UserRepository;
import sk.hfa.auth.service.interfaces.IUserService;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        if (Objects.isNull(username) || username.isEmpty())
            return Optional.empty();

        return userRepository.findByUsername(username);
    }

}
