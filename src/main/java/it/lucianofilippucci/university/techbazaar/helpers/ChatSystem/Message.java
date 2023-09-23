package it.lucianofilippucci.university.techbazaar.helpers.ChatSystem;

import lombok.*;

import java.time.LocalDateTime;

//Definition of the Single Message.
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String messageId;
    private String message;
    private int ownerId;
    private LocalDateTime sent;
    private boolean isRead;


}
