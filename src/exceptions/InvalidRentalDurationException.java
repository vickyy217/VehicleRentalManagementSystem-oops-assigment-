package exceptions;

/**
 * ============================================================================
 * JAVA CONCEPT 4: EXCEPTION HANDLING (Custom Checked Exception)
 * ============================================================================
 * InvalidRentalDurationException is thrown when a customer requests an
 * invalid rental duration, such as 0 days, negative days, or out-of-range values.
 */
public class InvalidRentalDurationException extends Exception {

    public InvalidRentalDurationException(String message) {
        super(message);
    }
}
