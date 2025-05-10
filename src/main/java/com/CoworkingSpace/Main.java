package com.CoworkingSpace.Main;

import com.CoworkingSpace.entities.Workspace;
import com.CoworkingSpace.repositories.WorkspaceRepository;
import com.CoworkingSpace.services.AdminService;
import com.CoworkingSpace.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final AdminService adminService;
    private final UserService userService;
    private final WorkspaceRepository workspaceRepository;
    private final Scanner scanner = new Scanner(System.in);

    public Main(AdminService adminService, UserService userService,
                WorkspaceRepository workspaceRepository) {
        this.adminService = adminService;
        this.userService = userService;
        this.workspaceRepository = workspaceRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {

        System.out.println("=== Coworking Space Reservation System ===");

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Select option: ");

            int choice = readIntInput();

            switch (choice) {
                case 1 -> adminMenu();
                case 2 -> userMenu();
                case 3 -> {
                    System.out.println("Thank you for using our system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }



    private void adminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add new workspace");
            System.out.println("2. Remove workspace");
            System.out.println("3. View all reservations");
            System.out.println("4. View all workspaces");
            System.out.println("5. Back to main menu");
            System.out.print("Select option: ");

            int choice = readIntInput();

            switch (choice) {
                case 1 -> addWorkspace();
                case 2 -> removeWorkspace();
                case 3 -> viewAllReservations();
                case 4 -> viewAllWorkspaces();
                case 5 -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addWorkspace() {
        System.out.print("Enter workspace type: ");
        String type = scanner.nextLine();

        System.out.print("Enter hourly price: ");
        float price = readFloatInput();

        Workspace workspace = new Workspace(type, price);
        workspaceRepository.save(workspace);
        System.out.println("Workspace added successfully!");
    }

    private void removeWorkspace() {
        System.out.print("Enter workspace ID to remove: ");
        Long id = readLongInput();

        workspaceRepository.findById(id).ifPresentOrElse(
                workspace -> {
                    workspaceRepository.delete(workspace);
                    System.out.println("Workspace removed successfully!");
                },
                () -> System.out.println("Workspace not found.")
        );
    }

    private void viewAllReservations() {
        // Реализация просмотра всех бронирований через adminService
    }

    private void viewAllWorkspaces() {
        workspaceRepository.findAll().forEach(ws ->
                System.out.printf("%d\t%-15s\t$%.2f%n",
                        ws.getId(), ws.getType(), ws.getPrice()));
    }

    private void userMenu() {
        System.out.print("\nEnter your name: ");
        String userName = scanner.nextLine();

        while (true) {
            System.out.println("\nUser Menu (" + userName + "):");
            System.out.println("1. Browse available spaces");
            System.out.println("2. Make a reservation");
            System.out.println("3. View my reservations");
            System.out.println("4. Cancel a reservation");
            System.out.println("5. Back to main menu");
            System.out.print("Select option: ");

            int choice = readIntInput();

            switch (choice) {
                case 1 -> viewAllWorkspaces();
                case 2 -> makeReservation(userName);
                case 3 -> viewUserReservations(userName);
                case 4 -> cancelReservation(userName);
                case 5 -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void makeReservation(String userName) {
        viewAllWorkspaces();

        System.out.print("Enter workspace ID: ");
        Long workspaceId = readLongInput();

        System.out.print("Enter start time (yyyy-MM-dd HH:mm): ");
        LocalDateTime startTime = readDateTimeInput();

        System.out.print("Enter end time (yyyy-MM-dd HH:mm): ");
        LocalDateTime endTime = readDateTimeInput();

        if (userService.makeReservation(userName, startTime, endTime, workspaceId)) {
            System.out.println("Reservation successful!");
        } else {
            System.out.println("Failed to make reservation. The workspace may be unavailable.");
        }
    }

    private void viewUserReservations(String userName) {
        userService.getUserReservations(userName).forEach(res ->
                System.out.printf("- ID: %d, Workspace: %s, From: %s, To: %s%n",
                        res.getId(),
                        res.getWorkspaceType(),
                        res.getStartTime().format(TIME_FORMAT),
                        res.getEndTime().format(TIME_FORMAT)));
    }

    private void cancelReservation(String userName) {
        viewUserReservations(userName);

        System.out.print("Enter reservation ID to cancel: ");
        Long reservationId = readLongInput();

        if (userService.cancelReservation(reservationId, userName)) {
            System.out.println("Reservation canceled successfully!");
        } else {
            System.out.println("No reservation found with this ID.");
        }
    }

    // Методы для ввода данных
    private int readIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private Long readLongInput() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private float readFloatInput() {
        while (true) {
            try {
                return Float.parseFloat(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private LocalDateTime readDateTimeInput() {
        while (true) {
            try {
                return LocalDateTime.parse(scanner.nextLine(), TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.print("Invalid format. Please use yyyy-MM-dd HH:mm: ");
            }
        }
    }
}