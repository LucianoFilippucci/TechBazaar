package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String username;
    private String email;
    private String password;
}
