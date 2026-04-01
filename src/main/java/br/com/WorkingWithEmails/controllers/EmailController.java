package br.com.WorkingWithEmails.controllers;

import br.com.WorkingWithEmails.controllers.docs.EmailControllerDocs;
import br.com.WorkingWithEmails.data.dto.request.EmailRequestDTO;
import br.com.WorkingWithEmails.services.EmailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailServices services;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequest) {
        services.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("e-mail sent with success!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile) {
        return null;
    }
}