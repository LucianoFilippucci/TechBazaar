package it.lucianofilippucci.university.techbazaar.helpers.Entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetails {

    private int from; // UserId/StoreId
    private String subject;
    private String message;
    private boolean isRead;
}
