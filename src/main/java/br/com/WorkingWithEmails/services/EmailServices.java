package br.com.WorkingWithEmails.services;

import br.com.WorkingWithEmails.config.EmailConfig;
import br.com.WorkingWithEmails.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServices {

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private EmailConfig emailConfigs;

    public void sendSimpleEmail(String to, String subject, String body) {
        emailSender
                .to(to)
                .withSubject(subject)
                .withMessage(body)
                .send(emailConfigs);
    }
}
