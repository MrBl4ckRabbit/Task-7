package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RoleServiceImpl;
import ru.itmentor.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    private void addAllRolesToModel(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> users = userService.findUsersByRole();
        model.addAttribute("users", users);
        return "admin/all";
    }

    @GetMapping("/{id}")
    public String readUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "admin/show";
    }

    @GetMapping("/createUser")
    public String showCreateForm(Model model) {
        User user = new User();
        Role defaultRole = roleService.findByName("ROLE_USER");
        user.getRoles().add(defaultRole);
        model.addAttribute("user", user);
        addAllRolesToModel(model);
        return "admin/new";
    }

//    @PostMapping()
//    public String createUser(@ModelAttribute("user") User user) {
//        userService.saveUser(user);
//        return "redirect:/admin/users";
//    }
@PostMapping()
public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);
        return "admin/new";
    }
    userService.saveUser(user);
    return "redirect:/admin/users";
}

    @GetMapping("/editUser/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUser(id);
        List<Role> roles = roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roles);
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String master(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = roleService.findAll();
            model.addAttribute("allRoles", roles);
            return "admin/edit";
        }
        userService.master(id, user);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}



