package com.example.shopmecommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 256, nullable = false)
    private String name;
    @Column(unique = true, length = 256, nullable = false)
    private String alias;
    @Column(length = 512, nullable = false, name = "short_description")
    private String shortDescription;
    @Column(length = 4096, nullable = false, name = "full_description")
    private String fullDescription;

    @CreatedDate
    @Column(name = "created_time")
    private Date createdTime;
    @LastModifiedDate
    @Column(name = "updated_time")
    private Date updatedTime;

    private boolean enabled;
    @Column(name = "in_stock")
    private boolean inStock;
    private float cost;

    private float price;
    @Column(name = "discount_percent")
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    @ManyToOne
    @JoinColumn(name ="category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name ="brand_id")
    private Brand brand;
}
