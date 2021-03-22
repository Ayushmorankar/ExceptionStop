package com.ayush.discussionforum.service;

import com.ayush.discussionforum.model.NotificationEmail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class MailService {

    @Autowired
    private MailContentBuilder mailContentBuilder;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${email.from}")
    private String emailSendFrom;

    @Async
    public void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(emailSendFrom);
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try{
            javaMailSender.send(mimeMessagePreparator);
        }catch (MailException e){
            throw new ResponseStatusException(HttpStatus.valueOf(500), "Couldn't send email");
        }
    }
}
