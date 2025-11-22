package com.dev.pernambox.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailService {
    /*
    Isso aqui ta funcionando retirei o erro da verificação do spring
    com esse SuppressWarnings pq o spring nao reconhece as variaveis
    de email que estao setadas na env.
    */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String emailTo, String subject, String bodyEmail) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(emailTo);
        mensagem.setSubject(subject);
        mensagem.setText(bodyEmail);
        mailSender.send(mensagem);
    }
}
