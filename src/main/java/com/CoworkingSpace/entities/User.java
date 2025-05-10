package com.CoworkingSpace;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    public void setUserName(String userName) {
        this.userName = userName;

    }
}