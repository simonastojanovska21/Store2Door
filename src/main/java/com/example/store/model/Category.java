package com.example.store.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String image;

    @OneToMany
    private List<Category> subCategories;

    public Category(String name)
    {
        this.name=name;
        this.subCategories=new ArrayList<>();
    }
}
