package com.example.SecureLogin2.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    // Método simples para envio de mensagem de texto
    public void enviarEmail(String destinatario, String assunto, String texto) {
        try {
            if (mailSender != null) {
                SimpleMailMessage mensagem = new SimpleMailMessage();
                mensagem.setTo(destinatario);
                mensagem.setSubject(assunto);
                mensagem.setText(texto);
                mailSender.send(mensagem);
                System.out.println("Email enviado com sucesso para: " + destinatario);
            } else {
                System.out.println("JavaMailSender não configurado. Mensagem simulada:");
                System.out.println("Para: " + destinatario);
                System.out.println("Assunto: " + assunto);
                System.out.println("Texto:\n" + texto);
            }
        } catch (Exception e) {
            System.out.println("Erro ao enviar email para " + destinatario + ": " + e.getMessage());
        }
    }
}
