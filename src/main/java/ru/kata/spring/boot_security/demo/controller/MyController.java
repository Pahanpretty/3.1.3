package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.persistence.Access;
import java.security.Principal;

@Controller
public class MyController {

    private final UserServiceImpl userService;

    @Autowired
    public MyController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAuthorityPage() {
        return "view_for_all_employees";
    }

    @GetMapping("/user")
    public String showUser(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        model.addAttribute("users1", user);
    return "/user";
   }
}
