package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon", schema = "techbazaar")
public class CouponEntity {
    @Id
    @Column(name = "coupon_code", nullable = false)
    private String code;

    @Basic
    @Column(name = "discount", nullable = false)
    private int discount;

    @Basic
    @Column(name = "category")
    private String category;

    @ManyToOne
    @JoinColumn(name = "emitter")
    private UserEntity store;

    @Basic
    @Column(name = "used")
    private int timesUsed;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration")
    @CreationTimestamp
    private Date expirationDate;

    @Basic
    @Column(name = "max_uses")
    private int maxUses;

    @ManyToMany
    @JoinTable(name = "user_coupon",
    joinColumns = @JoinColumn(name = "coupon_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> users;


}
