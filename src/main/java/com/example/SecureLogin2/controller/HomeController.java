package com.example.SecureLogin2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.SecureLogin2.application.User;
import com.example.SecureLogin2.application.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    // Rota protegida: somente quem estiver autenticado tem acesso
    @GetMapping({"/", "/home"})
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String login = authentication.getName();

            // Busca os dados do usuário logado na lista
            User usuario = userService.buscarPorUsernameOuEmail(login);

            if (usuario != null) {
                model.addAttribute("usuario", usuario);
            } else {
                model.addAttribute("nomeIdentificador", login);
            }
        }
        return "home";
    }
}
