package com.dbwiki.controllers;

import com.dbwiki.models.Personagem;
import com.dbwiki.services.PersonagemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/wiki")
public class WikiController {

    private final PersonagemService personagemService;

    @Autowired
    public WikiController(PersonagemService personagemService) {
        this.personagemService = personagemService;
    }

    // Lista todos os personagens
    @GetMapping("/personagens")
    public String listPersonagens(Model model) {
        List<Personagem> personagens = personagemService.getAllPersonagens();
        model.addAttribute("personagens", personagens);
        return "personagens"; // personagens.html
    }

    // Detalhes de um personagem
    @GetMapping("/personagem/{id}")
    public String personagemDetalhes(@PathVariable Long id, Model model) {
        Personagem personagem = personagemService.getPersonagemById(id).orElse(null);
        if (personagem == null) {
            return "redirect:/wiki/personagens";
        }
        model.addAttribute("personagem", personagem);
        return "personagem-detalhes"; 
    }

    
    @GetMapping("/add-personagem")
    public String addPersonagemPage(HttpSession session, Model model) {
        if (session.getAttribute("loggedUser") == null || 
            !"ADMIN".equals(((com.dbwiki.models.User) session.getAttribute("loggedUser")).getRole())) {
            return "redirect:/auth/login";
        }
        model.addAttribute("personagem", new Personagem());
        return "add-personagem"; 
    }

    @PostMapping("/add-personagem")
    public String addPersonagemSubmit(@ModelAttribute Personagem personagem, HttpSession session) {
        if (session.getAttribute("loggedUser") == null || 
            !"ADMIN".equals(((com.dbwiki.models.User) session.getAttribute("loggedUser")).getRole())) {
            return "redirect:/auth/login";
        }
        personagemService.addPersonagem(personagem);
        return "redirect:/wiki/personagens";
    }
}
