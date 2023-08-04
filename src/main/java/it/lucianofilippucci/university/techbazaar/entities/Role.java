package it.lucianofilippucci.university.techbazaar.entities;

import it.lucianofilippucci.university.techbazaar.helpers.model.ERole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20)
    private ERole roleName;

    public Role(ERole name) {
        this.roleName = name;
    }
}
