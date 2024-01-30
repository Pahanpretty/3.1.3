package ru.kata.spring.boot_security.demo.dao;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDAO extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u")
    List<User> getAllUsers();

    User getUserById(Long userId);

    void addUser(User user);


    void deleteUser(Long userId);

    public void createRolesIfNotExist();

    void deleteUserByName(String name);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    User findByName(String name);
}
