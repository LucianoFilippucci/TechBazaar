package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "product_resources", schema = "techbazaar", catalog = "")
public class ProductResourcesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "resource_type")
    private String resourceType;
    @Basic
    @Column(name = "resource_path")
    private String resourcePath;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private ProductEntity productByProductId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResourcesEntity that = (ProductResourcesEntity) o;
        return productId == that.productId && Objects.equals(resourceType, that.resourceType) && Objects.equals(resourcePath, that.resourcePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, resourceType, resourcePath);
    }

    public ProductEntity getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(ProductEntity productByProductId) {
        this.productByProductId = productByProductId;
    }
}
