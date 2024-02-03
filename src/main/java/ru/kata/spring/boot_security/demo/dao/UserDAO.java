package ru.kata.spring.boot_security.demo.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.model.User;

public interface UserDAO extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.name = :name")
    User findByName(String name);

    User getUserById(Long userId);

    void deleteUserById(Long userId);

    void deleteUserByName(String name);
}
