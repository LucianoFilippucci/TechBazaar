package it.lucianofilippucci.university.techbazaar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "user", schema = "techbazaar")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Basic
    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Basic
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Field("cartId")
    private String cartId;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<OrderEntity> orderEntities;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<UserAddressEntity> userAddressEntities;

}
