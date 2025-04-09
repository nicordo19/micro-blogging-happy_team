package com.microblog.app.controllers;

import com.microblog.app.models.User;
import com.microblog.app.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Hello Ada Tech 🚀");
        return "home";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("users", new User());
        return "inscription";

    }
    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/connexion";
    }
}
