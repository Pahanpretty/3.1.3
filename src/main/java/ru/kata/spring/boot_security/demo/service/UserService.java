package ru.kata.spring.boot_security.demo.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    @Query("SELECT u FROM User u")
    List<User> getAllUsers();

    void addUser(User user);

    void updateUser(User user, Set<Role> roles);

    User getUserById(Long id);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    User getUserByName(String name);

    void createRolesIfNotExist();

    void deleteUserById(Long id);

    void deleteUserByName(String name);

    Set<GrantedAuthority> getAuthorities(User user);

}
