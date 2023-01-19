package com.itproger.shop.controllers;

import com.itproger.shop.models.Role;
import com.itproger.shop.models.User;
import com.itproger.shop.repo.ItemRepository;
import com.itproger.shop.repo.UserRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String user(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("email", user.getEmail());
        return "user";
    }

    @PostMapping("/user/update")
    public String userUpdate(Principal principal, User userForm){
        User user = userRepository.findByUsername(principal.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        System.out.println(userForm.getRoles());
        user.setRoles(userForm.getRoles());
        userRepository.save(user);
        return "redirect:/user";
    }

    @GetMapping("/reg")
    public String reg(@RequestParam(name = "error", defaultValue = "", required = false) String error, Model model) {
        if(error.equals("username")) {
            model.addAttribute("error", "Такой логин пользователя уже занят");
        }
        return "reg";
    }

    @PostMapping("/reg")
    public String addUser(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password) {
        if(userRepository.findByUsername(username) != null) {
            return "redirect:/reg?error=username";
        }

        password = passwordEncoder.encode(password);
        User user = new User(username, password, email, true, Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }

}
