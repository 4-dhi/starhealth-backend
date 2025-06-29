package com.satheesh.starhealth.service;

import com.satheesh.starhealth.dto.FormSubmissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.to:abcd@gmail.com}")
    private String toEmail;

    @Value("${app.email.from:${spring.mail.username}}")
    private String fromEmail;

    public void sendFormSubmissionEmail(FormSubmissionDto formData) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(buildEmailSubject());

        String emailBody = buildEmailBody(formData);
        message.setText(emailBody);

        mailSender.send(message);
    }

    private String buildEmailSubject() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
        String timestamp = now.format(formatter);

        return "New Form Submission - Insurance Quote Request #" + timestamp;
    }

    private String buildEmailBody(FormSubmissionDto formData) {
        StringBuilder body = new StringBuilder();
        body.append("New Form Submission Received\n\n");
        body.append("Details:\n");
        body.append("Name: ").append(formData.getName()).append("\n");
        body.append("Email: ").append(formData.getEmail()).append("\n");
        body.append("Phone: ").append(formData.getPhone()).append("\n");
        body.append("Insurance Needs: ").append(formData.getNeeds() != null ? formData.getNeeds() : "Not specified").append("\n\n");
        body.append("Please follow up with the customer as needed.\n");

        return body.toString();
    }
}