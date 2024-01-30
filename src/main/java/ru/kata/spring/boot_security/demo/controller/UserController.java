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
import java.util.List;


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

    @GetMapping("/")
    public String getUsersListForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/list";
    }

    ////////////////////////////////create new user////////////////////////////////////////

//    @GetMapping("/new")
//    public String getNewUserForm(@ModelAttribute("user") User user) {
//        return "/new_user";
//    }
//    @PostMapping("/new")
//    public String creatNewUser(@ModelAttribute User user) {
//        userService.addUser(user);
//        return "redirect:/";
//    }
    @GetMapping("/new")
    public String getNewUserForm(@ModelAttribute("user") User user, Model model) {
        List<Role> roles = roleDAO.findAll();
        model.addAttribute("allRoles", roles);
        return "/new_user";
    }

    @PostMapping("/new")
    public String creatNewUser(@ModelAttribute ("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/new_user";
        }
        userService.addUser(user);
        return "redirect:/list";
    }


//////////////////////////////edit/update/showById/////////////////////////////////////////////

    @GetMapping("/edit")
    public ModelAndView editUserForm(@RequestParam Long id) {
        ModelAndView mav = new ModelAndView("/edit_user");
        User user = userService.getUserById(id);
        mav.addObject("user", user);
        return mav;
    }

//    @PostMapping("/admin/edit")
//    public String editUser(@ModelAttribute("user") User user) {
//        userService.updateUser(user);
//        return "redirect:/";
//    }

    @PostMapping("/edit")
    public String updateUser(@RequestParam("id") long id,
                             @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/edit_user";
        userService.updateUser(user, user.getRoles());
        return "redirect:/admin";
    }

/////////////////////////////////delete///////////////////////////////////////////////

//    @PostMapping("/admin")
//    public String deleteUser(@RequestParam("id") Long id) {
//        userService.deleteUser(id);
//        return "redirect:/";
//    }

    @PostMapping("/")
    public String deleteUser(@RequestParam("name") String name) {
        userService.deleteUserByName(name);
        return "redirect:/admin";
    }

//    @PostMapping("/admin")
//    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
//                              @RequestParam(required = true, defaultValue = "" ) String action,
//                              Model model) {
//        if (action.equals("delete")){
//            userService.deleteUser(userId);
//        }
//        return "redirect:/admin";
//    }

}
