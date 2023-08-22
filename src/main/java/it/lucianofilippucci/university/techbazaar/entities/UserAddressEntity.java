package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_address", schema = "techbazaar")
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int addressId;

    @Basic
    @Column(name = "address_state")
    @Enumerated(EnumType.STRING)
    private AddressState addressState;

    @Basic
    @Column(name = "provincia", length = 45)
    private String provincia;

    @Basic
    @Column(name = "via", length = 45)
    private String via;

    @Basic
    @Column(name = "civico", length = 12)
    private String civico;

    @Basic
    @Column(name = "name", length = 45)
    private String addressName;

    @Basic
    @Column(name = "default")
    private int isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public enum AddressState {
        ITALY,
        USA
    }

    @Override
    public String toString() {
        return this.via + ", " + this.civico + ", " + this.provincia + ", " + this.addressState;
    }

}


