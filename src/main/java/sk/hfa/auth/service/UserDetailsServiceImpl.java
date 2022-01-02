package sk.hfa.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.hfa.auth.domain.User;
import sk.hfa.auth.domain.UserDetailsImpl;
import sk.hfa.auth.service.interfaces.IUserService;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserService userService;

    public UserDetailsServiceImpl(IUserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username)
                               .orElseThrow(() -> new UsernameNotFoundException("User not found with the provided username: [" + username + "]"));

        return UserDetailsImpl.build(user);
    }

}
