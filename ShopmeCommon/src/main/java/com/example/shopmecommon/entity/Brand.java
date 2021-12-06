package com.example.shopmecommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 45, unique = true)
    private String name;
    @Column(nullable = false, length = 128)
    private String photo;
    @ManyToMany
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    
    public Brand(String name) {
        this.name = name;
        this.photo = "brand-logo.png";
    }

    public Brand(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    //khong lien quan den db
    @Transient
    public String getPathImage() {
        if(this.photo == null || this.photo.equals("brand-logo.png")) {
            System.out.println(this.photo);
            return "/images/brand-logo.png";
        }
        return "/brand-photos/" + this.id + "/" + this.photo;
    }

}
