package com.CoworkingSpace;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdminService {

    private final WorkspaceRepository workspaceRepository;

    public AdminService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public void addWorkspace(Workspace workspace) {
        workspaceRepository.save(workspace);
    }

    public boolean removeWorkspace(Long id) {
        if (workspaceRepository.existsById(id)) {
            workspaceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Workspace> getWorkspaces() {
        return workspaceRepository.findAll();
    }

    public void viewAllWorkspaces() {
        List<Workspace> workspaces = getWorkspaces();
        if (workspaces.isEmpty()) {
            System.out.println("No workspaces available.");
        } else {
            for (Workspace w : workspaces) {
                System.out.printf("ID: %d | Type: %s | Price: %.2f%n", w.getId(), w.getType(), w.getPrice());
            }
        }
    }

    public void viewAllReservations() {
        System.out.println("Reservation viewing not implemented yet.");
    }
}
