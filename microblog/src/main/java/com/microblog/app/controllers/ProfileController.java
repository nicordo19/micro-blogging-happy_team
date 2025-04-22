package com.microblog.app.controllers;

import com.microblog.app.repositories.UserRepository;
import com.microblog.app.models.User;
import com.microblog.app.repositories.PostRepository;
import com.microblog.app.models.Post;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.sql.Timestamp;

@Controller
public class ProfileController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile/{user_id}")
    public String profile(@PathVariable Long user_id, Model model) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found!"));
        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "profile";
    }

    @GetMapping("/posts/new")
    public String showPostForm(@RequestParam("user_id") Long user_id, Model model) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found!"));
        Post post = new Post();
        post.setUser(user);
        model.addAttribute("post", post);
        return "new-post";
    }

    @PostMapping("/posts/new")
    public String createPost(@ModelAttribute Post post,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            String uploadDir = "src/main/resources/static/uploads/";
            Files.createDirectories(Paths.get(uploadDir));
            Path filePath = Paths.get(uploadDir + filename);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            post.setImage("/uploads/" + filename);
        }
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        postRepository.save(post);
        return "redirect:/profile/" + post.getUser().getId();
    }
}
