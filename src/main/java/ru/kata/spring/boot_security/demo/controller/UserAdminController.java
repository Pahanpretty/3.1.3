package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class UserAdminController {

    private final UserService userService;
    private final RoleService roleService;

    public UserAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping
    public String allUserTable(Model model, Principal principal) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("currentUserEmail", principal.getName());
        model.addAttribute("currentUserRoles", userService.findByEmail(principal.getName()).getAuthorities());

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("user", currentUser);
        return "users";
    }


    @GetMapping("/user")
    public String showUser(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "user";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String createUpdateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.findAll());
        return "users";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        userService.updateUser(id, user);
        redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        return "redirect:/admin";
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users";
    }


}
