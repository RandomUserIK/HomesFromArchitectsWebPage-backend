package sk.hfa.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.hfa.auth.domain.User;
import sk.hfa.auth.service.interfaces.IUserService;

@Slf4j
@Component
public class AdminRunner implements CommandLineRunner {

    private final IUserService userService;

    @Value("${hfa.server.admin.username}")
    private String username;

    @Value("${hfa.server.admin.password}")
    private String password;

    @Autowired
    public AdminRunner(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Creating dummy admin user...");
        User adminUser = new User(username, password);
        userService.saveUser(adminUser);
        log.info("Dummy admin user successfully created.");
    }

}
