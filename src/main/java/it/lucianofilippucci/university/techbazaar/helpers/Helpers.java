package it.lucianofilippucci.university.techbazaar.helpers;

import it.lucianofilippucci.university.techbazaar.entities.UserEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Helpers {

    public static String GenerateUID() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS");
        String timestamp = currentTime.format(formatter);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return timestamp + uuid;
    }

    public static void NotifyOrderStatusUpdate(UserEntity user) {
        //TODO: Notify To both WebApp and email.
    }
}
