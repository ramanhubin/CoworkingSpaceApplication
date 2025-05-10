package com.CoworkingSpace;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name="Reservation")
public class Reservation  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(nullable = false)
    private String userName;


    @Column(nullable = false)
    private LocalDateTime startTime;


    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "workspace id")
    private Workspace workspace;

    @ManyToOne  // Добавляем обратную связь
    @JoinColumn(name = "user_id")
    private User user;

    public String getUsername() {
        return userName;
    }
    public Workspace getWorkspace(){
        return workspace;
    }
    public String getWorkspaceType(){
       return workspace.getType();
    }

    public Long getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }



    public Object getUserName() {
        return userName;
    }
}