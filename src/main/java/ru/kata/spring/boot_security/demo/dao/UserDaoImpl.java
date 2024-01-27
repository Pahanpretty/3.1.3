package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Component
public class UserDaoImpl implements UserDAO {

    private RoleDAO roleRepository;
    private UserRepository userRepository;

    @Autowired
    public UserDaoImpl(RoleDAO roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager
                .createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    @Override
    public void addUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            throw new RuntimeException(user.getUsername() + "already exist");
        }
        createRolesIfNotExist();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void createRolesIfNotExist() {
        if (roleRepository.getByUsername("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if (roleRepository.getByUsername("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
    }

    @Override
    public void updateUser(User user) {
        User userEdit = entityManager.find(User.class, user.getId());
        if (userEdit != null) {
            userEdit.setName(user.getName());
            userEdit.setSurname(user.getSurname());
            userEdit.setAge(user.getAge());
            userEdit.setLevel(user.getLevel());
            userEdit.setPoints(user.getPoints());
        }
        entityManager.merge(userEdit);
    }

    @Override
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

//    @Override
//    public void deleteUserByUsername(String username) {
//        User user = entityManager.find(User.class, username);
//        if (user != null) {
//            entityManager.remove(user);
//        }
//    }

    @Override
    public User getUserById(Long userId) {
        return getAllUsers()
                .stream().filter(user -> Objects.equals(user.getId(), userId))
                .findAny()
                .orElse(null);
    }
}
