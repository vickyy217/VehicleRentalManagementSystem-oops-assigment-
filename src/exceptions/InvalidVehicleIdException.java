package exceptions;

/**
 * ============================================================================
 * JAVA CONCEPT 4: EXCEPTION HANDLING (Custom Checked Exception)
 * ============================================================================
 * InvalidVehicleIdException is thrown when a provided vehicle identifier
 * does not match any vehicle in the fleet inventory, or is blank/invalid.
 */
public class InvalidVehicleIdException extends Exception {

    public InvalidVehicleIdException(String message) {
        super(message);
    }
}
