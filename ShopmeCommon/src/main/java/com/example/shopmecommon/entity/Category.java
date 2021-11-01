package com.example.shopmecommon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 128, nullable = false, unique = true)
    private String name;
    @Column(length = 64, nullable = false, unique = true)
    private String alias;
    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Set<Category> children = new HashSet<>();

    public Category(Long id) {
        this.id = id;
    }
    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public  Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    //khong lien quan den db
    @Transient
    public String getPathImage() {
        return "/category-photos/" + this.id + "/" + this.image;
    }
}
