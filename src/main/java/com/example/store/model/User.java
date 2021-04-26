package com.example.store.model;

import com.example.store.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "store_users")
public class User {
    @Id
    private String username;

    private String password;

    private String name;

    private String surname;

    private String address;

    private String city;

    private String telephone;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    public String getUsername() {
        return username;
    }
}
