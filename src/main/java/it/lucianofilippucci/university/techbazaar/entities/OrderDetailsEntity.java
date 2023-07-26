package it.lucianofilippucci.university.techbazaar.entities;//package it.lucianofilippucci.university.techbazaar.entities;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@EqualsAndHashCode
//@ToString
//@Entity
//@Table(name = "order_details", schema = "techbazaar")
//public class OrderDetailsEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "detail_id", nullable = false)
//    private int orderDetailId;
//
//    @Basic
//    @Column(name = "quantity", nullable = false)
//    private int productQuantity;
//
//    @Basic
//    @Column(name = "unitary_price", nullable = false)
//    private float productUnitaryPrice;
//
//    @Basic
//    @Column(name = "total", nullable = false)
//    private float total;
//
//    @Basic
//    @Column(name = "iva", nullable = false)
//    private int iva;
//
//
////    @ManyToOne
////    @JoinColumn(name = "product_id")
////    @JsonIgnore
////    private ProductEntity product;
////
////    @ManyToOne
////    @JoinColumn(name = "order_id")
////    @JsonIgnore
////    private OrderEntity order;
//
//
//}
