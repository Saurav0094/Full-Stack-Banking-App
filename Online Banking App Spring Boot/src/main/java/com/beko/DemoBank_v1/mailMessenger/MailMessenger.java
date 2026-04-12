package com.beko.DemoBank_v1.mailMessenger;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailMessenger {

    private static JavaMailSender mailSender;

    public MailMessenger(JavaMailSender mailSender) {
        MailMessenger.mailSender = mailSender;
    }

    public static void htmlEmailMessenger(String from, String toMail, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper htmlMessage = new MimeMessageHelper(message, true);

        htmlMessage.setTo(toMail);
        htmlMessage.setFrom(from);
        htmlMessage.setSubject(subject);
        htmlMessage.setText(body, true);

        mailSender.send(message);
    }
}