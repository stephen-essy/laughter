package com.laughter.laughter.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.laughter.laughter.DTO.UserDTO;
import com.laughter.laughter.Entity.User;

@Service

public class EmailServices {
    @Autowired
    private JavaMailSender mailSender;
    @Async
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

    @Async 
    public void sendRecoveryPassword(String to, User user, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("LAUGHTER,TEARS AND GOOSEBUMPS");
        message.setText("Dear " + user.getName() + " ,\n\n" +
                "we are glad that you kept the secret code we sent during account creation" + "\n\n"
                + "we encourage that you remember your password this is  due to our security policies  that we do not  allow password recovery for more than 5 times,"
                + "\n\n"
                + "Prior to this " + password + " is your password, if  not please email us !" + "\n\n"
                + "its Laughters");
        mailSender.send(message);
    }

}
