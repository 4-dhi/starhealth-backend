package com.satheesh.starhealth.controller;


import com.satheesh.starhealth.dto.FormSubmissionDto;
import com.satheesh.starhealth.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Configure this properly for production
public class FormController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/submit-form")
    public ResponseEntity<Map<String, String>> submitForm(@Valid @RequestBody FormSubmissionDto formData) {
        try {
            emailService.sendFormSubmissionEmail(formData);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Form submitted successfully!");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to submit form. Please try again.");

            return ResponseEntity.badRequest().body(response);
        }
    }

    // Alternative endpoint for form-encoded data (if you want to keep the original HTML form)
    @PostMapping("/submit-form-encoded")
    public ResponseEntity<String> submitFormEncoded(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam(required = false) String needs) {

        try {
            FormSubmissionDto formData = new FormSubmissionDto(name, email, phone, needs);
            emailService.sendFormSubmissionEmail(formData);

            return ResponseEntity.ok("Form submitted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to submit form. Please try again.");
        }
    }
}