package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RoleServiceImpl;
import ru.itmentor.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")//добавил api  чтобы избежать конфликтов с обычным контроллером
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class MyRestControllers {
    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public MyRestControllers(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userServiceImpl.findUsersByRole();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userServiceImpl.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        Role defaultRole = roleServiceImpl.findByName("ROLE_USER");
        user.getRoles().add(defaultRole);
        userServiceImpl.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
                                           @RequestBody @Valid User user,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        userServiceImpl.master(id, user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}













































