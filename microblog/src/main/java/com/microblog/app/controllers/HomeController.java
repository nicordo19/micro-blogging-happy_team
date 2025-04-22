package com.microblog.app.controllers;

import com.microblog.app.models.User;
import com.microblog.app.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(HttpSession session,Model model) {
        User user = (User) session.getAttribute("loggedUser");

        if (user != null) {
            model.addAttribute("username", user.getUsername());
        } else {
            model.addAttribute("username", "Invité");
        }
        model.addAttribute("message", "Hello Ada Tech 🚀");
        return "home";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("user", users);
        return "users";
    }
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "inscription";

    }
    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/connexion";
    }
    @GetMapping("/connexion")
    public String showLoginForm(Model model){
        model.addAttribute("user", new User());
        return "connexion";
    }
    @PostMapping("/connexion")
    public String processLogin(@ModelAttribute("user") User user, Model model,HttpSession session) {
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            // ✅ Connexion réussie (mot de passe en clair ici)
            session.setAttribute("loggedUser", existingUser);
            return "redirect:/"; // page d’accueil après connexion
        }

        // ❌ Email ou mot de passe incorrect
        model.addAttribute("error", "Identifiants incorrects");
        return "connexion";
    }

}
