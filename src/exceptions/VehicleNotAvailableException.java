package exceptions;

/**
 * ============================================================================
 * JAVA CONCEPT 4: EXCEPTION HANDLING (Custom Checked Exception)
 * ============================================================================
 * VehicleNotAvailableException is thrown when a customer or worker thread
 * attempts to book a vehicle that has already been rented out.
 * 
 * In Java, creating custom exception classes allows an application to model
 * business-specific error states explicitly and provide descriptive feedback.
 */
public class VehicleNotAvailableException extends Exception {

    public VehicleNotAvailableException(String message) {
        super(message);
    }

    public VehicleNotAvailableException(String vehicleId, String reason) {
        super("Vehicle [" + vehicleId + "] is not available for rental: " + reason);
    }
}
