package ru.itmentor.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.itmentor.spring.boot_security.demo.service.RoleService;
import ru.itmentor.spring.boot_security.demo.service.UserService;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public SpringBootSecurityDemoApplication(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    public static void main(String[] args) {

        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
    }
}
