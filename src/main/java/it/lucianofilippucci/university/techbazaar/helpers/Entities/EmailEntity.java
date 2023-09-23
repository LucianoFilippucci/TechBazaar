package it.lucianofilippucci.university.techbazaar.helpers.Entities;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailEntity {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachments;
    private String sender;
}
