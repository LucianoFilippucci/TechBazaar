package it.lucianofilippucci.university.techbazaar.helpers;

import it.lucianofilippucci.university.techbazaar.helpers.Entities.EmailEntity;

public interface EmailService {
    boolean sendSimpleMail(EmailEntity email);
    String sendMailWithAttachments(EmailEntity email);

}
