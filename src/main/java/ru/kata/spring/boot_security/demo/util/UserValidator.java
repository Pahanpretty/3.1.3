package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

@Component
public class UserValidator implements Validator {
    private final UserServiceImpl userService;

    @Autowired
    public UserValidator(UserServiceImpl userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        try {
            userService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return;
        }
        errors.rejectValue("username", "", "User is exists");
    }
}
