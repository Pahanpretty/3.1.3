package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.*;


@Controller
@RequestMapping("/admin")
public class UserController {

    private final UserServiceImpl userService;
    private final RoleDAO roleDAO;
    @Autowired
    public UserController(UserServiceImpl userService, UserValidator userValidator, RoleDAO roleDAO) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleDAO = roleDAO;
    }

    private final UserValidator userValidator;

    @GetMapping("")
    public String getUsersListForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/list";
    }

    ////////////////////////////////create new user////////////////////////////////////////

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleDAO.findAll());
        return "new_user";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleDAO.findAll());
            return "new_user";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

//////////////////////////////edit/update/showById/////////////////////////////////////////////

    @GetMapping("/edit")
    public ModelAndView editUserForm(@RequestParam Long id) {
        ModelAndView mav = new ModelAndView("/edit_user");
        User user = userService.getUserById(id);
        Optional<Role> roles = roleDAO.getAllById(id);
        mav.addObject("roles", roles);
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute User user,  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleDAO.findAll());
            return "edit_user";
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

//    @PostMapping("/edit")
//    public String saveUser(@Valid User user,
//                           BindingResult bindingResult, Model model) {
//        List<Role> roleList = new ArrayList<>(roleDAO.findAll());
//        Set<Role> roleSet = new HashSet<>(roleList);
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("user", user);
//            model.addAttribute("roles", roleDAO.findAll());
//            return "edit_user";
//        }
//        userService.updateUser(user, roleSet);
//        return "redirect:/admin";
//    }

//    @PostMapping("/edit")
//    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()){
//            return "/list";
//        }
//        userService.updateUser(user, user.getRoles());
//        return "redirect:/admin";
//    }

/////////////////////////////////delete///////////////////////////////////////////////

    @PostMapping("")
    public String deleteUser(@RequestParam("name") String name) {
        userService.deleteUserByName(name);
        return "redirect:/admin";
    }


}
