package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean updateUser(Long id, User updatedUser) {
        Optional<User> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            User existingUser = userFromDb.get();

            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAge(updatedUser.getAge());

            if (!updatedUser.getPassword().equals(existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            existingUser.setRoles(updatedUser.getRoles());

            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

