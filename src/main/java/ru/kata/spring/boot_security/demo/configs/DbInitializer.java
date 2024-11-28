package ru.kata.spring.boot_security.demo.configs;


import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
public class DbInitializer {

    private final UserService userService;
    private final RoleService roleService;

    public DbInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void init() {
        roleService.saveRole(new Role(1L, "ROLE_ADMIN"));
        roleService.saveRole(new Role(2L, "ROLE_USER"));

        Role adminRole = roleService.findById(1L);
        Role userRole = roleService.findById(2L);

        userService.saveUser(new User("admin", "admin", 30, "admin@mail.ru", "admin",
                adminRole != null ? List.of(adminRole) : List.of()));
        userService.saveUser(new User("user", "user", 35, "user@mail.ru", "user",
                userRole != null ? List.of(userRole) : List.of()));
    }


}
