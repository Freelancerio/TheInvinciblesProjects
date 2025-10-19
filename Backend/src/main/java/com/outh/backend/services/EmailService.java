package com.outh.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVoucherEmail(String to, String voucherCode, String amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Voucher Code");
        message.setText("Hello!\n\nYou have successfully withdrawn " + amount + " coins.\n" +
                "Your voucher code is: " + voucherCode + "\n\nEnjoy!");
        mailSender.send(message);
    }
}
