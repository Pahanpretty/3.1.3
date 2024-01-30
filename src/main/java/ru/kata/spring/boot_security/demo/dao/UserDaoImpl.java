//package ru.kata.spring.boot_security.demo.dao;
//
//import com.sun.xml.bind.v2.model.core.ID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import ru.kata.spring.boot_security.demo.model.Role;
//import ru.kata.spring.boot_security.demo.model.User;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.List;
//import java.util.Objects;
//
//@Component
//public class UserDaoImpl extends SimpleJpaRepository<User, ID> {
//
//    private RoleDAO roleRepository;
//    private UserRepository userRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Autowired
//    public UserDaoImpl(RoleDAO roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.roleRepository = roleRepository;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public List<User> getAllUsers() {
//        return entityManager
//                .createQuery("SELECT u FROM User u", User.class)
//                .getResultList();
//    }
//
//
//    public void addUser(User user) {
//        User userFromDB = userRepository.findByName(user.getUsername());
//
//        if (userFromDB != null) {
//            throw new RuntimeException(user.getUsername() + "already exist");
//        }
//        createRolesIfNotExist();
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }
//
//
//    public void createRolesIfNotExist() {
//        if (roleRepository.getAllById(2L).isEmpty()) {
//            roleRepository.save(new Role("ROLE_USER"));
//        }
//        if (roleRepository.getAllById(1L).isEmpty()) {
//            roleRepository.save(new Role("ROLE_ADMIN"));
//        }
//    }
//
//
//    public void updateUser(User user) {
//        User userEdit = entityManager.find(User.class, user.getId());
//        if (userEdit != null) {
//            userEdit.setName(user.getName());
//            userEdit.setSurname(user.getSurname());
//            userEdit.setAge(user.getAge());
//            userEdit.setLevel(user.getLevel());
//            userEdit.setPoints(user.getPoints());
//        }
//        entityManager.merge(userEdit);
//    }
//
//
//    public void deleteUser(Long id) {
//        User user = entityManager.find(User.class, id);
//        if (user != null) {
//            entityManager.remove(user);
//        }
//    }
//
////    @Override
////    public void deleteUserByUsername(String username) {
////        User user = entityManager.find(User.class, username);
////        if (user != null) {
////            entityManager.remove(user);
////        }
////    }
//
//
//    public User getUserById(Long userId) {
//        return getAllUsers()
//                .stream().filter(user -> Objects.equals(user.getId(), userId))
//                .findAny()
//                .orElse(null);
//    }
//}
