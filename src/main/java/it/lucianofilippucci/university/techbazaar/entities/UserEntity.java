package it.lucianofilippucci.university.techbazaar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
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
    @JsonIgnore
    private String password;

    @Basic
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Basic
    @Column(name = "p_iva")
    private long pIva = 0L;

    @JsonIgnore
    @OneToMany(mappedBy = "store")
    private Collection<ProductEntity> storeProducts;

    @Field("cartId")
    private String cartId;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<OrderEntity> orderEntities;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<UserAddressEntity> userAddressEntities;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<ProductReviewsEntity> reviews;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private Collection<CouponEntity> coupons;

    public UserEntity(String username, String email, String encode) {
        this.username = username;
        this.email = email;
        this.password = encode;
    }

    @ManyToMany(mappedBy = "users")
    private Set<CouponEntity> usedCoupon;

    @OneToMany(mappedBy = "winner", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AuctionEntity> auctionsWon;
}
