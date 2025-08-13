package com.laughter.laughter.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.laughter.laughter.DTO.UserDTO;

@Service
public class EmailServices {
    @Autowired
    private JavaMailSender mailSender;

    public void sendAccountCreationVerification(String to, UserDTO userDTO, String recoverString) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("LAUGHTER,TEARS AND GOOSEBUMBS");
        message.setText("Dear " + userDTO.getName()+ "  ,\n\n" +
                "Your Account has been Created Successfully" + "\n\n" +
                recoverString +" "+"is your secret recovery String keep it secret, and use it to recover you password"
                + "\n\n " +
                "it Laughters");
        mailSender.send(message);
    }

}
