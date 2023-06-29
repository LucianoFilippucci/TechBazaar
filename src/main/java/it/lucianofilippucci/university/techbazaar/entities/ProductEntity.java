package it.lucianofilippucci.university.techbazaar.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "product", schema = "techbazaar", catalog = "")
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "product_name")
    private String productName;
    @Basic
    @Column(name = "product_description")
    private String productDescription;
    @Basic
    @Column(name = "category")
    private String category;
    @Basic
    @Column(name = "product_price")
    private double productPrice;
    @Basic
    @Column(name = "product_quantity")
    private int productQuantity;
    @Basic
    @Column(name = "product_total_selt")
    private Integer productTotalSelt;

    @Basic
    @Column(name = "store_identifier")
    private String storeIdentifier;

    public String getStoreIdentifier() { return storeIdentifier; }
    public void setStoreIdentifier(String storeIdentifier) { this.storeIdentifier = storeIdentifier; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Integer getProductTotalSelt() {
        return productTotalSelt;
    }

    public void setProductTotalSelt(Integer productTotalSelt) {
        this.productTotalSelt = productTotalSelt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductEntity that = (ProductEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.productPrice, productPrice) != 0) return false;
        if (productQuantity != that.productQuantity) return false;
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) return false;
        if (productDescription != null ? !productDescription.equals(that.productDescription) : that.productDescription != null)
            return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (productTotalSelt != null ? !productTotalSelt.equals(that.productTotalSelt) : that.productTotalSelt != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (productDescription != null ? productDescription.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        temp = Double.doubleToLongBits(productPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + productQuantity;
        result = 31 * result + (productTotalSelt != null ? productTotalSelt.hashCode() : 0);
        return result;
    }
}
