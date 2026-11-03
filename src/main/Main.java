package main;

import exceptions.InvalidRentalDurationException;
import exceptions.InvalidVehicleIdException;
import exceptions.VehicleNotAvailableException;
import model.RentalRecord;
import service.RentalProcessingThread;
import service.RentalService;

import java.util.Arrays;
import java.util.Scanner;

/**
 * ============================================================================
 * JAVA CONCEPTS APPLIED: PACKAGES, INTERFACES, MULTITHREADING, EXCEPTION HANDLING
 * ============================================================================
 * Main.java provides the interactive console user interface for the
 * Vehicle Rental Management System.
 * 
 * Demonstrates:
 * 1. Packages: imports and connects 'model', 'service', and 'exceptions' packages.
 * 2. Interfaces: interacts with polymorphic Vehicle instances through Rentable behaviors.
 * 3. Multithreading: dispatches rental operations onto separate worker threads
 *    and can trigger multi-threaded concurrency simulations.
 * 4. Exception Handling: uses structured try-catch-finally blocks to gracefully
 *    intercept checked exceptions, invalid numbers, and edge-cases without crashing.
 */
public class Main {

    private final RentalService rentalService;
    private final Scanner scanner;

    public Main() {
        this.rentalService = new RentalService();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.runMenu();
    }

    /**
     * Main application loop displaying the interactive menu.
     */
    public void runMenu() {
        boolean exitRequested = false;

        System.out.println("===============================================================================");
        System.out.println("            WELCOME TO THE VEHICLE RENTAL MANAGEMENT SYSTEM                   ");
        System.out.println("   Demonstrating: Interfaces | Packages | Multithreading | Exception Handling   ");
        System.out.println("===============================================================================");

        while (!exitRequested) {
            displayMenuOptions();

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleDisplayAvailableVehicles();
                    break;
                case 2:
                    handleRentVehicle();
                    break;
                case 3:
                    handleReturnVehicle();
                    break;
                case 4:
                    handleViewRentalDetails();
                    break;
                case 5:
                    exitRequested = true;
                    handleExit();
                    break;
                case 6:
                    // Multithreading demonstration option
                    handleRunConcurrencyDemo();
                    break;
                default:
                    System.out.println("\n[!] Invalid selection! Please enter a number between 1 and 5 (or 6 for demo).\n");
            }
        }
    }

    /**
     * Prints the primary user interface menu.
     */
    private void displayMenuOptions() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("                                  MAIN MENU                                    ");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("  1. Display Available Vehicles");
        System.out.println("  2. Rent a Vehicle (Processed on a separate worker thread)");
        System.out.println("  3. Return a Vehicle");
        System.out.println("  4. View Rental Details");
        System.out.println("  5. Exit");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("  6. [DEMO] Run Multithreading Concurrency Test (Simulate Race Condition)");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.print("Enter your choice (1-5, or 6): ");
    }

    /**
     * Reads and validates the user's integer menu choice.
     * Demonstrates exception handling for non-integer inputs.
     */
    private int getUserChoice() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // Handled gracefully without crashing
            return -1;
        }
    }

    /**
     * Menu Option 1: Display Available Vehicles.
     */
    private void handleDisplayAvailableVehicles() {
        rentalService.displayAvailableVehicles();
    }

    /**
     * Menu Option 2: Rent a Vehicle.
     * Demonstrates:
     * - Multithreading: Creating and executing a worker thread (RentalProcessingThread).
     * - Exception Handling: Comprehensive try-catch-finally block.
     */
    private void handleRentVehicle() {
        System.out.println("\n===============================================================================");
        System.out.println("                              RENT A VEHICLE                                   ");
        System.out.println("===============================================================================");

        // First display current available vehicles to guide the user
        rentalService.displayAvailableVehicles();

        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine().trim();

        if (customerName.isEmpty()) {
            System.out.println("[ERROR] Customer name cannot be empty. Returning to menu.\n");
            return;
        }

        System.out.print("Enter Vehicle ID to Rent (e.g. CAR-101, BIK-201): ");
        String vehicleId = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter Number of Rental Days: ");
        String daysInput = scanner.nextLine().trim();

        int rentalDays;
        try {
            rentalDays = Integer.parseInt(daysInput);
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid number format! Rental days must be a whole integer.\n");
            return;
        }

        System.out.println("\n--- Initiating Multithreaded Rental Processing ---");

        // --------------------------------------------------------------------
        // JAVA CONCEPT 3 & 4: MULTITHREADING AND EXCEPTION HANDLING
        // --------------------------------------------------------------------
        RentalProcessingThread rentalTask = new RentalProcessingThread(
                rentalService, customerName, vehicleId, rentalDays);
        Thread workerThread = new Thread(rentalTask, "Worker-Thread-Rent-" + vehicleId);

        try {
            // Launch the background thread
            workerThread.start();

            System.out.println("[Main Thread] Worker thread started. Awaiting thread completion...");

            // Wait for the worker thread to finish its job
            workerThread.join();

            // Inspect the outcome from the worker thread
            if (rentalTask.isSuccessful()) {
                RentalRecord record = rentalTask.getResultRecord();
                System.out.println("\n===============================================================================");
                System.out.println("                      RENTAL RECEIPT & CONFIRMATION                            ");
                System.out.println("===============================================================================");
                System.out.printf("  Receipt ID      : %s%n", record.getRentalId());
                System.out.printf("  Customer Name   : %s%n", record.getCustomerName());
                System.out.printf("  Vehicle ID      : %s (%s %s)%n",
                        record.getVehicle().getVehicleId(),
                        record.getVehicle().getBrand(),
                        record.getVehicle().getModel());
                System.out.printf("  Duration        : %d days%n", record.getRentalDays());
                System.out.printf("  Daily Rate      : $%.2f%n", record.getVehicle().getRentalPricePerDay());
                System.out.printf("  Total Amount    : $%.2f%n", record.getTotalCost());
                System.out.println("===============================================================================\n");
            } else {
                Exception error = rentalTask.getFailureException();
                if (error instanceof VehicleNotAvailableException) {
                    System.out.printf("%n[BOOKING FAILED] %s%n%n", error.getMessage());
                } else if (error instanceof InvalidVehicleIdException) {
                    System.out.printf("%n[INVALID ID] %s%n%n", error.getMessage());
                } else if (error instanceof InvalidRentalDurationException) {
                    System.out.printf("%n[INVALID DURATION] %s%n%n", error.getMessage());
                } else {
                    System.out.printf("%n[ERROR] An error occurred: %s%n%n",
                            (error != null ? error.getMessage() : "Unknown"));
                }
            }

        } catch (InterruptedException e) {
            System.err.println("[Main Thread] Thread interrupted during rental processing: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            // ----------------------------------------------------------------
            // JAVA CONCEPT 4: FINALLY BLOCK
            // The finally block always executes regardless of whether an
            // exception occurred or was caught.
            // ----------------------------------------------------------------
            System.out.println("[FINALLY BLOCK] Rental transaction workflow completed. UI state restored.\n");
        }
    }

    /**
     * Menu Option 3: Return a Vehicle.
     * Demonstrates exception handling for invalid vehicle IDs and unavailable vehicles.
     */
    private void handleReturnVehicle() {
        System.out.println("\n===============================================================================");
        System.out.println("                              RETURN A VEHICLE                                 ");
        System.out.println("===============================================================================");

        System.out.print("Enter Vehicle ID to Return (e.g. CAR-101, BIK-201): ");
        String vehicleId = scanner.nextLine().trim().toUpperCase();

        try {
            boolean success = rentalService.returnVehicle(vehicleId);

            if (success) {
                System.out.println("\n[SUCCESS] Vehicle [" + vehicleId + "] has been successfully returned!");
                System.out.println("The vehicle status is now restored to 'AVAILABLE' in the fleet.\n");
            } else {
                System.out.println("\n[NOTICE] Vehicle [" + vehicleId + "] was not marked as rented. It is already available!\n");
            }

        } catch (InvalidVehicleIdException e) {
            System.out.println("\n[ERROR] Return failed: " + e.getMessage() + "\n");
        } finally {
            System.out.println("[FINALLY BLOCK] Return transaction process concluded.\n");
        }
    }

    /**
     * Menu Option 4: View Rental Details.
     */
    private void handleViewRentalDetails() {
        rentalService.displayRentalDetails();
    }

    /**
     * Menu Option 5: Exit application.
     */
    private void handleExit() {
        System.out.println("\n===============================================================================");
        System.out.println("       Thank you for using the Vehicle Rental Management System! Goodbye!      ");
        System.out.println("===============================================================================\n");
        scanner.close();
    }

    /**
     * Menu Option 6: Demonstrates Multithreading Concurrency & Synchronization.
     */
    private void handleRunConcurrencyDemo() {
        System.out.println("\nSelect a vehicle ID for the concurrency simulation (Default: CAR-101):");
        System.out.print("Press ENTER to use 'CAR-101' or enter a specific ID: ");
        String inputId = scanner.nextLine().trim().toUpperCase();
        if (inputId.isEmpty()) {
            inputId = "CAR-101";
        }

        // Simulate 4 concurrent customers competing for the single vehicle
        rentalService.demonstrateConcurrency(
                inputId,
                Arrays.asList("Alice (Online Web)", "Bob (Mobile App)", "Charlie (In-Store Kiosk)", "David (Call Center)"),
                3);
    }
}
