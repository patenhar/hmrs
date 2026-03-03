package com.hrms.backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final S3Service s3Service;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailService(JavaMailSender mailSender, S3Service s3Service) {
        this.mailSender = mailSender;
        this.s3Service = s3Service;
    }

    @Async("emailExecutor")
    public Future<String> sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        return CompletableFuture.completedFuture("Mail Sent Successfully...");
    }

    @Async("emailExecutor")
    public Future<String> sendMailWithAttachment(String to, String subject, String body, String fileUrl) throws MessagingException, IOException {
        String s3Key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        byte[] fileBytes = s3Service.downloadFile(s3Key);
        InputStreamSource attachment = new ByteArrayResource(fileBytes);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        helper.addAttachment(s3Key, attachment);

        mailSender.send(message);
        return CompletableFuture.completedFuture("Mail Sent Successfully with Attachment...");
    }
}