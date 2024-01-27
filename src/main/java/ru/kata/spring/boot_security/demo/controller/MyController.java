package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.persistence.Access;

@Controller
public class MyController {

    private final UserServiceImpl userService;

    @Autowired
    public MyController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getInfoForAllEmps() {
        return "view_for_all_employees";
    }

    @GetMapping("/hr_info")
    public String getInfoOnlyForHR() {
        return "view_for_hr";
    }

    @GetMapping("/manager_info")
    public String getInfoOnlyForManagers() {
        return "view_for_managers";
    }

    @GetMapping("/user")
    public String getUserForm(Model model) {
        model.addAttribute( "users1", userService.getAllUsers());
        return "/user";
    }

//    @GetMapping("")
//    public String getUserForm(Model model, Principal principal) {
//        User user = userService.findByName(principal.getName());
//        model.addAttribute("users1", user);
//   }
}
