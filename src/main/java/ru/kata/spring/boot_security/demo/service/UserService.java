package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    void addUser(User user);

    void updateUser(User user);
    void updateUser(User user, Set<Role> roles);

    void deleteUser(Long id);

    User getUserById(Long id);

}
