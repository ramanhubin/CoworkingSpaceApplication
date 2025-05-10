package com.CoworkingSpace;

import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    private Long workspaceId;
    private String workspaceType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String userName; // Добавлен новый параметр

    // Конструктор со всеми полями
    public ReservationDto(Long id, Long workspaceId, String workspaceType,
                          LocalDateTime startTime, LocalDateTime endTime, String userName) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.workspaceType = workspaceType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userName = userName;
    }

    // Геттеры
    public Long getId() { return id; }
    public Long getWorkspaceId() { return workspaceId; }
    public String getWorkspaceType() { return workspaceType; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getUserName() { return userName; }
}