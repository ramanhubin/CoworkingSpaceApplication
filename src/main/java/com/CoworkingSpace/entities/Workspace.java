package com.CoworkingSpace;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*

;
@Entity
@Table(name = "Workspace")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private float price;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>(); //List to store reservations connected to a current workspace

    public Workspace(String type, float price) {
        this.type = type;
        this.price = price;
    }
    public int getWorkspaceId() {
        return id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public boolean checkAvailability(LocalDateTime startTime, LocalDateTime endTime) {  //This method checks availability of workspace
        for (Reservation res : reservations) {
            if (startTime.isBefore(res.getEndTime()) && endTime.isAfter(res.getStartTime())) {
                return false;
            }
        }
        return true;
    }

    public boolean addReservation(Reservation reservation) { //Adding new reservation
        if (reservation != null) {
            return reservations.add(reservation);
        } else{
            System.out.println("Reservation can't be null");
        }
        return false;

    }

    public boolean removeReservation(String userName) {
        return reservations.removeIf(res -> res.getUsername().equals(userName));  //Deleting all the reservations connected to user
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }
}