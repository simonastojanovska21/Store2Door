package com.example.store.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stars;

    private String description;

    private LocalDateTime localDateTime;

    @ManyToOne
    private User user;

    public Review(Integer stars,String description,User user)
    {
        this.stars=stars;
        this.description=description;
        this.localDateTime=LocalDateTime.now();
        this.user=user;
    }
}
