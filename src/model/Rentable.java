package model;

import exceptions.InvalidRentalDurationException;
import exceptions.VehicleNotAvailableException;

/**
 * ============================================================================
 * JAVA CONCEPT 1: INTERFACES
 * ============================================================================
 * An Interface in Java is an abstract type used to specify a contract of behavior
 * that implementing classes must fulfill.
 * 
 * Key Characteristics of Interfaces:
 * 1. Abstraction: Defines method signatures without detailing implementation.
 * 2. Polymorphism: Different classes (Cars, Bikes, etc.) can be treated uniformly
 *    through the Rentable reference.
 * 3. Loose Coupling: Separates the declaration of rental operations from their
 *    specific vehicle implementations.
 */
public interface Rentable {

    /**
     * Rents the vehicle for a specified number of days.
     * 
     * @param days Number of days for the rental.
     * @throws VehicleNotAvailableException   If the vehicle is currently unavailable.
     * @throws InvalidRentalDurationException If the requested duration is <= 0.
     */
    void rent(int days) throws VehicleNotAvailableException, InvalidRentalDurationException;

    /**
     * Returns the rented vehicle back into the available pool.
     */
    void returnVehicle();

    /**
     * Calculates the total rental charges for the specified number of days.
     * 
     * @param days Number of days for the rental.
     * @return Total rental cost.
     */
    double calculateRentalCost(int days);

    /**
     * Checks if the vehicle is currently available for rent.
     * 
     * @return true if available, false if currently rented.
     */
    boolean isAvailable();
}
