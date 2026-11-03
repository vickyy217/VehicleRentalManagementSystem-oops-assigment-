package model;

import exceptions.InvalidRentalDurationException;
import exceptions.VehicleNotAvailableException;

/**
 * ============================================================================
 * JAVA CONCEPT 1 & 2: INTERFACES & PACKAGES (Base Abstract Class)
 * ============================================================================
 * Vehicle serves as the abstract base class for all rental vehicles.
 * 
 * 1. Interface Implementation: Vehicle implements the Rentable interface,
 *    providing concrete logic for availability management and rental cost.
 * 2. Encapsulation: All fields (vehicleId, brand, model, rentalPricePerDay,
 *    isAvailable) are declared private and accessed through public getters/setters.
 * 3. Inheritance: Car and Bike extend this base class and inherit common state.
 */
public abstract class Vehicle implements Rentable {

    // Encapsulated instance variables
    private String vehicleId;
    private String brand;
    private String model;
    private double rentalPricePerDay;
    private boolean isAvailable;

    /**
     * Parameterized Constructor for initializing common vehicle attributes.
     */
    public Vehicle(String vehicleId, String brand, String model, double rentalPricePerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.rentalPricePerDay = rentalPricePerDay;
        this.isAvailable = true; // Newly registered vehicles are available by default
    }

    // ------------------------------------------------------------------------
    // Encapsulation: Getters and Setters
    // ------------------------------------------------------------------------
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public void setRentalPricePerDay(double rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    // ------------------------------------------------------------------------
    // Implementation of Rentable Interface Methods
    // ------------------------------------------------------------------------
    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public void rent(int days) throws VehicleNotAvailableException, InvalidRentalDurationException {
        if (days <= 0) {
            throw new InvalidRentalDurationException(
                    "Rental duration must be at least 1 day. Requested: " + days + " days.");
        }
        if (!this.isAvailable) {
            throw new VehicleNotAvailableException(vehicleId,
                    "This vehicle is currently already rented to another customer.");
        }
        // Mark vehicle as rented
        this.isAvailable = false;
    }

    @Override
    public void returnVehicle() {
        // Mark vehicle as available
        this.isAvailable = true;
    }

    @Override
    public double calculateRentalCost(int days) {
        return this.rentalPricePerDay * days;
    }

    /**
     * Abstract method to display formatted vehicle details.
     * Subclasses (Car, Bike) implement this to display their unique attributes.
     */
    public abstract void displayDetails();

    @Override
    public String toString() {
        return String.format("[%s] %s %s ($%.2f/day) - %s",
                vehicleId, brand, model, rentalPricePerDay, (isAvailable ? "AVAILABLE" : "RENTED"));
    }
}
