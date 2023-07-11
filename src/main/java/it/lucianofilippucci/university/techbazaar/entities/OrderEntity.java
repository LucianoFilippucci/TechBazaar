package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "orders", schema = "techbazaar")
@NoArgsConstructor
public class OrderEntity {

    @Id
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", nullable = false)
    @CreationTimestamp
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Basic
    @Column(name = "shipping_addr", nullable = false, length = 255)
    private String shippingAddr;

    @Basic
    @Column(name = "total", nullable = false)
    private float orderTotal;

    @Basic
    @Column(name = "contact_info", length = 45)
    private String contactInfo;

    @Basic
    @Column(name = "note", length = 255)
    private String orderNotes;

    @Basic
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Basic
    @Column(name = "tracking_code")
    private String trackingCode;

    @Basic
    @Column(name = "express_courier")
    private String expressCourier;

    public enum OrderStatus {
        PENDING,
        SHIPPED,
        DELIVERED
    }


//    @OneToMany
//    @JsonIgnore
//    private Collection<OrderDetailsEntity> orderDetailsEntities;


}
