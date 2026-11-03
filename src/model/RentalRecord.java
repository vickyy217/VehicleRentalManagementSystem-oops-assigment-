package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * JAVA CONCEPT 2: PACKAGES (Data Model Class)
 * ============================================================================
 * RentalRecord stores transaction details for vehicle rentals.
 * It tracks customer info, rented vehicle, duration, total charges,
 * timestamp, and rental lifecycle status.
 */
public class RentalRecord {

    private String rentalId;
    private String customerName;
    private Vehicle vehicle;
    private int rentalDays;
    private double totalCost;
    private LocalDateTime rentalDate;
    private String status; // "ACTIVE" or "RETURNED"

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RentalRecord(String rentalId, String customerName, Vehicle vehicle, int rentalDays, double totalCost) {
        this.rentalId = rentalId;
        this.customerName = customerName;
        this.vehicle = vehicle;
        this.rentalDays = rentalDays;
        this.totalCost = totalCost;
        this.rentalDate = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    public String getRentalId() {
        return rentalId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public String getStatus() {
        return status;
    }

    public void markAsReturned() {
        this.status = "RETURNED";
    }

    /**
     * Prints a formatted row for rental records table.
     */
    public void displayRecord() {
        System.out.printf("| %-9s | %-12s | %-8s | %-16s | %-4d days | $%-9.2f | %-9s | %-16s |%n",
                rentalId,
                customerName,
                vehicle.getVehicleId(),
                vehicle.getBrand() + " " + vehicle.getModel(),
                rentalDays,
                totalCost,
                status,
                rentalDate.format(FORMATTER));
    }
}
