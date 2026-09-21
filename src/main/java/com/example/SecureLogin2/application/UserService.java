package com.example.SecureLogin2.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class UserService implements UserDetailsService {

    // Lista em memória para armazenar os usuários (sem banco de dados)
    private final List<User> usuarios = new ArrayList<>();

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // Cria um usuário inicial para facilitar os testes logo que o sistema inicia
    @PostConstruct
    public void inicializarUsuarios() {
        cadastrarUsuario("Administrador", "admin", "admin@pucminas.br", "admin123");
    }

    // Método que o Spring Security usa para validar o login (por username ou email)
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = buscarPorUsernameOuEmail(login);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + login);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    // Busca usuário por username ou por email na lista
    public User buscarPorUsernameOuEmail(String login) {
        for (User u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(login) || u.getEmail().equalsIgnoreCase(login)) {
                return u;
            }
        }
        return null;
    }

    // Verifica se já existe um usuário com o mesmo username
    public boolean existsByUsername(String username) {
        for (User u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // Verifica se já existe um usuário com o mesmo email
    public boolean existsByEmail(String email) {
        for (User u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // Cadastra um novo usuário adicionando-o na lista
    public User cadastrarUsuario(String nome, String username, String email, String senhaTextoPuro) {
        String senhaCriptografada = passwordEncoder.encode(senhaTextoPuro);
        User novoUsuario = new User(nome, username, email, senhaCriptografada);
        usuarios.add(novoUsuario);
        return novoUsuario;
    }

    // Processa a recuperação de senha
    public boolean recuperarSenha(String email) {
        User user = null;
        for (User u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                user = u;
                break;
            }
        }

        if (user == null) {
            return false;
        }

        // Gera uma senha temporária simples de 6 dígitos (ex: 742189)
        int codigo = 100000 + new Random().nextInt(900000);
        String novaSenha = String.valueOf(codigo);

        // Atualiza a senha do usuário com hash criptografado
        user.setPassword(passwordEncoder.encode(novaSenha));

        // Envia o e-mail
        String assunto = "Recuperação de Senha - SecureLogin";
        String mensagem = "Olá, " + user.getNome() + "!\n\n"
                + "Sua nova senha de acesso é: " + novaSenha + "\n\n"
                + "Equipe SecureLogin PUC";

        emailService.enviarEmail(user.getEmail(), assunto, mensagem);
        System.out.println("Nova senha para " + user.getEmail() + ": " + novaSenha);

        return true;
    }
}
