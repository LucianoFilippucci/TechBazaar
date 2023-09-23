package it.lucianofilippucci.university.techbazaar.services;

import it.lucianofilippucci.university.techbazaar.entities.UserEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.NotificationEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.EmailEntity;
import it.lucianofilippucci.university.techbazaar.helpers.Entities.NotificationDetails;
import it.lucianofilippucci.university.techbazaar.helpers.MailerService;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.repositories.mongodb.NotificationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {


    NotificationRepository notificationRepository;
    UserService userService;
    MailerService mailerService;

    @Value("${spring.mail.username}")
    String fromEmail;
    String toEmail;

    public NotificationService(NotificationRepository notificationRepository, UserService userService, MailerService mailerService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.mailerService = mailerService;
    }

    public int totalUnreadNotifications(int userId) throws ObjectNotFoundException{
        Optional<NotificationEntity> notEnt = this.notificationRepository.findNotificationEntitiesByUserId(userId);
        if(notEnt.isEmpty()) throw new ObjectNotFoundException();
        List<NotificationDetails> notDetails = notEnt.get().getNotificationList();

        int total = 0;
        for(NotificationDetails notDet : notDetails) {
            if(!notDet.isRead()) total++;
        }

        return total;
    }

    @Transactional
    public void sendMessage(int from, int to, String subject, String message) throws ObjectNotFoundException {
        NotificationDetails details = new NotificationDetails(from, subject, message, false);

        if(from != 0) {
            Optional<UserEntity> fromEntity = this.userService.getById(from);
            fromEmail = fromEntity.orElseThrow().getEmail();
        } else {
            fromEmail = "lucianofilippucci@gmail.com";
        }
        Optional<UserEntity> toEntity = this.userService.getById(to);

        this.toEmail = toEntity.orElseThrow().getEmail();
        toEntity.get().setNotifications(toEntity.get().getNotifications() + 1);
        Optional<NotificationEntity> entity = this.notificationRepository.findNotificationEntitiesByUserId(toEntity.get().getUserId());
        if(entity.isEmpty()) throw new ObjectNotFoundException();
        entity.get().getNotificationList().add(details);
        this.notificationRepository.save(entity.get());
        //TODO: Send Email & RabbitMQ notification to Angular
        EmailEntity email = new EmailEntity(this.toEmail, message, subject, "", this.fromEmail);
        this.mailerService.sendSimpleMail(email);
    }
}
