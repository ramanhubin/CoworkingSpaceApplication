package com.CoworkingSpace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    @Query("SELECT r FROM Reservation r WHERE r.workspace.id = :workspaceId " +
            "AND ((r.startTime < :endTime) AND (r.endTime > :startTime))")
    List<Reservation> findByWorkspaceIdAndTimeRange(
            @Param("workspaceId") Long workspaceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);


    Optional<Reservation> findById(Long id);
    List<Reservation> findAll();
    void deleteById(Long id);
    List<Reservation> findByUserName(String userName);
}