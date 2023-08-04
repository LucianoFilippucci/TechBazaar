package it.lucianofilippucci.university.techbazaar.helpers;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type;
    private int id;
    private String username;
    private String email;
    private List<String> roles;
    private String cartId;

    public JwtResponse(String jwt, Integer id, String username, String email, List<String> roles, String cartId) {
        this.token = jwt;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.cartId = cartId;
    }
}
