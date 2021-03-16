package sk.hfa.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (Objects.isNull(username) || username.isEmpty())
            return Optional.empty();

        return userRepository.findByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        if (Objects.isNull(user))
            return null;

        encodePassword(user);
        return userRepository.save(user);
    }

    private void encodePassword(User user) {
        if (Objects.isNull(user.getPassword()) || user.getPassword().isEmpty())
            throw new IllegalArgumentException("User has no password");

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    }

}
