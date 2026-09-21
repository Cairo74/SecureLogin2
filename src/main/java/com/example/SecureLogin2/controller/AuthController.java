package com.example.SecureLogin2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SecureLogin2.application.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // GET /login - Exibe a tela de login
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            @RequestParam(value = "recovered", required = false) String recovered,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Usuário, email ou senha incorretos.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Você encerrou sua sessão com sucesso.");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Conta criada com sucesso! Faça seu login.");
        }
        if (recovered != null) {
            model.addAttribute("successMessage", "Uma nova senha foi gerada e enviada para o seu email.");
        }

        return "login";
    }

    // GET /register - Exibe a tela de cadastro
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // POST /register - Processa o formulário de cadastro
    @PostMapping("/register")
    public String processRegister(
            @RequestParam("nome") String nome,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        // Devolve os dados preenchidos para não precisar digitar de novo caso dê erro
        model.addAttribute("nome", nome);
        model.addAttribute("username", username);
        model.addAttribute("email", email);

        // 1. Validação de campos obrigatórios vazios
        if (nome.isBlank() || username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("errorMessage", "Preencha todos os campos obrigatórios.");
            return "register";
        }

        nome = nome.trim();
        username = username.trim();
        email = email.trim().toLowerCase();

        // 2. Validação simples de email
        if (!email.contains("@") || !email.contains(".")) {
            model.addAttribute("errorMessage", "Informe um endereço de email válido.");
            return "register";
        }

        // 3. Validação de tamanho mínimo de senha
        if (password.length() < 6) {
            model.addAttribute("errorMessage", "A senha deve ter pelo menos 6 caracteres.");
            return "register";
        }

        // 4. Validação de compatibilidade de senhas
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "As senhas digitadas não são iguais.");
            return "register";
        }

        // 5. Validação de usuário duplicado
        if (userService.existsByUsername(username)) {
            model.addAttribute("errorMessage", "Este nome de usuário já está em uso.");
            return "register";
        }

        // 6. Validação de email duplicado
        if (userService.existsByEmail(email)) {
            model.addAttribute("errorMessage", "Já existe uma conta com este email.");
            return "register";
        }

        // Se passar em todas as validações, cadastra o usuário com senha criptografada
        userService.cadastrarUsuario(nome, username, email, password);

        // Redireciona para login informando que o cadastro foi feito
        return "redirect:/login?registered=true";
    }

    // GET /recoverpassword - Exibe a tela de recuperação de senha
    @GetMapping("/recoverpassword")
    public String recoverPasswordPage() {
        return "recoverpassword";
    }

    // POST /recoverpassword - Processa o pedido de nova senha por email
    @PostMapping("/recoverpassword")
    public String processRecoverPassword(
            @RequestParam("email") String email,
            Model model) {

        if (email.isBlank()) {
            model.addAttribute("errorMessage", "Por favor, digite o seu email.");
            return "recoverpassword";
        }

        email = email.trim().toLowerCase();
        model.addAttribute("email", email);

        if (!email.contains("@") || !email.contains(".")) {
            model.addAttribute("errorMessage", "Informe um endereço de email válido.");
            return "recoverpassword";
        }

        // Tenta recuperar a senha
        boolean sucesso = userService.recuperarSenha(email);

        if (!sucesso) {
            model.addAttribute("errorMessage", "Nenhum usuário foi encontrado com este email.");
            return "recoverpassword";
        }

        // Redireciona para o login com mensagem de sucesso
        return "redirect:/login?recovered=true";
    }
}
