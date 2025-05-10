package com.CoworkingSpace;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final ReservationRepository reservationRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    public UserService(ReservationRepository reservationRepository,
                       WorkspaceRepository workspaceRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }
    public User getOrCreateUser(String userName) {
        return userRepository.findByUserName(userName)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUserName(userName);
                    return userRepository.save(newUser);
                });
    }
    public boolean makeReservation(String userName, LocalDateTime startTime,
                                   LocalDateTime endTime, Long workspaceId) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found"));

        // Проверка доступности временного слота
        boolean isAvailable = reservationRepository
                .findByWorkspaceIdAndTimeRange(workspaceId, startTime, endTime)
                .isEmpty();

        if (!isAvailable) {
            return false;
        }

        Reservation reservation = new Reservation();
        reservation.setUserName(userName);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setWorkspace(workspace);

        reservationRepository.save(reservation);
        return true;
    }

    public boolean cancelReservation(Long reservationId, String userName) {
        return reservationRepository.findById(reservationId)
                .filter(res -> res.getUserName().equals(userName))
                .map(res -> {
                    reservationRepository.delete(res);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<ReservationDto> getUserReservations(String userName) {
        return reservationRepository.findByUserName(userName).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReservationDto convertToDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                (long) reservation.getWorkspace().getId(),
                reservation.getWorkspace().getType(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getUsername()
        );
    }
}