package com.ra.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailService {
//    @Autowired
//    private JavaMailSender mailSender;

    public void sendMail(String to, String subject) throws MessagingException {
        //creating message
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(from);
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(msg);
//        //sending message
//        mailSender.send(message);

        // html mail
//        MimeMessage message= mailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true);
////        mimeMessageHelper.setCc("hondayamato996@gmail.com");
//        mimeMessageHelper.setTo(to);
//        mimeMessageHelper.setSubject(subject);
//        mimeMessageHelper.setText("<html><body><h1 style='color: red'>Bạn đã bị hack nick</h1><img src='https://photo-cms-baophapluat.zadn.vn/w800/Uploaded/2021/jqkptqmv/2016_10_12/tam_mao4_walb.png'></body></html>",true);
//        FileSystemResource res = new FileSystemResource(new File("C:\\Users\\AD\\Downloads\\f23f647b8d208ec50cd2c5b9e4f9b089.jpg"));
//        mimeMessageHelper.addInline("avatar.jpg", res);
//        mailSender.send(message);
    }
}
