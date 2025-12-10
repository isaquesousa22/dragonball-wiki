package com.dbwiki.controllers;

import com.dbwiki.models.User;
import com.dbwiki.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

  
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        if (userService.authenticate(username, password)) {
            User user = userService.findByUsername(username).get();
            session.setAttribute("loggedUser", user);
            return "redirect:/"; 
        } else {
            model.addAttribute("error", "Usuário ou senha inválidos");
            return "login";
        }
    }

    
    @GetMapping("/register")
    public String registerPage() {
        return "register"; 
    }

  
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            model.addAttribute("error", "E-mail inválido.");
            return "register";
        }

        try {
            userService.registerUser(username, password, email, "USER");
            model.addAttribute("success", "Usuário registrado com sucesso! Faça login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
