package com.itproger.shop.controllers;

import com.itproger.shop.models.Item;
import com.itproger.shop.models.User;
import com.itproger.shop.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    public String admin(Model model){
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-panel";

    }

    @GetMapping("/admin/user-{id}")
    public String userReviews(@PathVariable(value = "id") long userId, Model model){
        User user = userRepository.findById(userId).orElse(new User());
        model.addAttribute("user", user);
        return "user-items";
    }
}
