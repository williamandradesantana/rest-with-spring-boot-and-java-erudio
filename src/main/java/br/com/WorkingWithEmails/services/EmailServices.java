package br.com.WorkingWithEmails.services;

import br.com.WorkingWithEmails.config.EmailConfig;
import br.com.WorkingWithEmails.data.dto.request.EmailRequestDTO;
import br.com.WorkingWithEmails.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServices {

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private EmailConfig emailConfigs;

    public void sendSimpleEmail(EmailRequestDTO emailRequest) {
        emailSender
                .to(emailRequest.getTo())
                .withSubject(emailRequest.getSubject())
                .withMessage(emailRequest.getBody())
                .send(emailConfigs);
    }
}
