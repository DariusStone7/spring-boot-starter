package com.spring_boot_starter.api.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendOtp(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("OTP Verification");
        message.setText("Your verification code is: " + otp + "\n\nExpires in 2 minutes.");
        mailSender.send(message);
    }
}
