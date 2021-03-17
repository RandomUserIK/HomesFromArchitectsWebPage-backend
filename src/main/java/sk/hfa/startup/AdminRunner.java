package sk.hfa.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.hfa.auth.domain.Role;
import sk.hfa.auth.domain.User;
import sk.hfa.auth.service.interfaces.IRoleService;
import sk.hfa.auth.service.interfaces.IUserService;

@Slf4j
@Component
public class AdminRunner implements CommandLineRunner {

    private final IUserService userService;
    private final IRoleService roleService;

    @Value("${hfa.server.admin.username}")
    private String username;

    @Value("${hfa.server.admin.password}")
    private String password;

    @Autowired
    public AdminRunner(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Creating dummy admin user...");
        User adminUser = new User(username, password);
        Role adminRole = new Role("ROLE_ADMIN");
        adminUser.getRoles().add(adminRole);
        adminRole.getUsers().add(adminUser);
        userService.saveUser(adminUser);
        roleService.save(adminRole);
        log.info("Dummy admin user successfully created.");
    }

}
