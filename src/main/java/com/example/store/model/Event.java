package com.example.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime finish;

    @ManyToOne
    private User customer;

    @OneToOne
    private ShoppingCart shoppingCart;

    public Event(String title, String description, LocalDateTime start, LocalDateTime finish,User customer,ShoppingCart shoppingCart)
    {
        this.title=title;
        this.description=description;
        this.start=start;
        this.finish=finish;
        this.customer=customer;
        this.shoppingCart=shoppingCart;
    }
}
