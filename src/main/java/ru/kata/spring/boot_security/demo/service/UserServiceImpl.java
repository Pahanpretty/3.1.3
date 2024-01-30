package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public void addUser(User user) {
        User userByIdFromDB = userDAO.getUserById(user.getId());

        if (userByIdFromDB == null) {
            createRolesIfNotExist();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDAO.save(user);
        } else throw new RuntimeException("User by id: " + user.getId() + " in DB already exist");
    }

    @Override
    @Transactional
    public void updateUser(User user, Set<Role> roles) {
        User userFromDB = userDAO.getUserById(user.getId());
        if (userFromDB != null) {
            userFromDB.setName(user.getName());
            userFromDB.setSurname(user.getSurname());
            userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
            userFromDB.setAge(user.getAge());
            userFromDB.setLevel(user.getLevel());
            userFromDB.setPoints(user.getPoints());
            userFromDB.setRoles(roles);
            userDAO.save(userFromDB);
        } else throw new RuntimeException("User with this parameters already exist");

    }

    @Override
    public User getUserById(Long id) {
        User user = userDAO.getUserById(id);
        if (user != null) {
            return userDAO.getUserById(id);
        } else throw new RuntimeException("User by id: " + id + " in DB not found");
    }

    @Override
    public User getUserByName(String name) {
        return userDAO.findByName(name);
    }

    @Override
    public void createRolesIfNotExist() {
        if (roleDAO.findById(1L).isEmpty()) {
            roleDAO.save(new Role(1L, "ROLE_ADMIN"));
        }
        if (roleDAO.findById(2L).isEmpty()) {
            roleDAO.save(new Role(2L, "ROLE_USER"));
        }
    }

    @Override
    public void deleteUserById(Long id) {
        userDAO.deleteUser(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userDAO.findByName(name);

        if (user == null) {
            throw new UsernameNotFoundException("User " + name + " not found");
        }
        return new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), user.getRoles());
    }

    @Override
    public Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    @Override
    public void deleteUserByName(String name) {
        User user = getUserByName(name);
        if (user != null) {
            userDAO.deleteUserByName(name);
        }
    }
}
