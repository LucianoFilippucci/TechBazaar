package it.lucianofilippucci.university.techbazaar.helpers;

import it.lucianofilippucci.university.techbazaar.helpers.Entities.EmailEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.hibernate.validator.cfg.defs.EmailDef;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class MailerService implements  EmailService {
    JavaMailSender javaMailSender;

    public MailerService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public boolean sendSimpleMail(EmailEntity email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(email.getSender());
            mailMessage.setTo(email.getRecipient());
            mailMessage.setText(email.getMsgBody());
            mailMessage.setSubject(email.getSubject());

            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String sendMailWithAttachments(EmailEntity email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        // TODO: edit the attachment to be firstly in a tmp folder then sent.
        try {
            // Set Multipart to true for attachments to be send
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(email.getSender());
            mimeMessageHelper.setTo(email.getRecipient());
            mimeMessageHelper.setText(email.getMsgBody());
            mimeMessageHelper.setSubject(email.getSubject());

            FileSystemResource file = new FileSystemResource(new File(email.getAttachments()));

            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            javaMailSender.send(mimeMessage);
            return "MailSent";
        } catch (MessagingException e) {
            return "Error.";
        }
    }
}
