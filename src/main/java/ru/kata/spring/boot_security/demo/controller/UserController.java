package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;


@Controller
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    public UserController() {
    }

    @GetMapping("/admin")
    public String getUsersListForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/list";
    }

    ////////////////////////////////create new user////////////////////////////////////////

    @GetMapping("/new")
    public String getNewUserForm(@ModelAttribute("user") User user) {
        return "/new_user";
    }

    @PostMapping("/new")
    public String creatNewUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/";
    }

//////////////////////////////edit/update/showById/////////////////////////////////////////////

    @GetMapping("/admin/edit")
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

    @PostMapping("/admin/edit")
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

    @PostMapping("/admin")
    public String deleteUser(@RequestParam("username") String username) {
        userService.deleteUserByUsername(username);
        return "redirect:/";
    }
}
