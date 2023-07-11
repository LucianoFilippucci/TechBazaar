package it.lucianofilippucci.university.techbazaar.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "store", schema = "techbazaar")
public class StoreEntity {

    @Id
    @Column(name = "store_id", nullable = false, length = 45)
    private String storeId;

    @Basic
    @Column(name = "store_name", nullable = false, length = 45)
    private String storeName;


    @JsonIgnore
    @OneToMany(mappedBy = "store")
    private Collection<ProductEntity> storeProducts;
}
