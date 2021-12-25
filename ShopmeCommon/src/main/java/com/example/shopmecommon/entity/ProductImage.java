package com.example.shopmecommon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @JsonIgnore
    private Product product;

    public ProductImage(String image, Product product) {
        this.image = image;
        this.product = product;
    }

    public ProductImage(Long id, String image, Product product) {
        this.id = id;
        this.image = image;
        this.product = product;
    }

    @Transient
    public String getProductImage() {
        return "/product-photos/" + product.getId() + "/extraImages/" + image;
    }
}
