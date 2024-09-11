package ru.itmentor.spring.boot_security.demo.service;


import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    User getUser(Long id);
    List<User> findUsersByRole();
    void saveUser(User user);
    void deleteUser(Long id);
    void updateUser(User user);
}
