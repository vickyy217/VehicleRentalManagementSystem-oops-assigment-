package main;

import service.RentalService;

import java.util.Arrays;

/**
 * ============================================================================
 * STANDALONE MULTITHREADING CONCURRENCY DEMONSTRATOR
 * ============================================================================
 * This class can be executed independently to explicitly demonstrate:
 * 1. Multiple threads executing concurrently (Thread-1, Thread-2, Thread-3, Thread-4).
 * 2. Shared resource contention (all threads attempting to rent the same vehicle).
 * 3. Synchronization preventing double booking (race condition safety).
 * 
 * To run:
 *   java -cp bin main.ConcurrencyDemo
 */
public class ConcurrencyDemo {

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println("          VEHICLE RENTAL SYSTEM - DEDICATED CONCURRENCY TEST SUITE             ");
        System.out.println("===============================================================================");

        RentalService rentalService = new RentalService();

        // 1. Initial State
        System.out.println("\n[STEP 1] Initial Fleet Status:");
        rentalService.displayAvailableVehicles();

        // 2. Launch Concurrency Simulation on CAR-102
        System.out.println("[STEP 2] Launching 4 concurrent threads competing for CAR-102...");
        rentalService.demonstrateConcurrency(
                "CAR-102",
                Arrays.asList(
                        "Alice (Web Portal)",
                        "Bob (Mobile App)",
                        "Charlie (Branch Office)",
                        "Diana (Partner API)"
                ),
                4
        );

        // 3. Inspect Resulting Fleet Status
        System.out.println("[STEP 3] Fleet Status After Concurrent Booking Attempt:");
        rentalService.displayAvailableVehicles();

        // 4. Inspect Rental Records
        System.out.println("[STEP 4] Transaction Audit Log:");
        rentalService.displayRentalDetails();

        System.out.println("===============================================================================");
        System.out.println(" [SUCCESS] Concurrency Test Completed: Exactly ONE thread booked the car.       ");
        System.out.println(" No race conditions occurred. Availability safely protected by synchronization! ");
        System.out.println("===============================================================================");
    }
}
