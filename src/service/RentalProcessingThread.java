package service;

import exceptions.InvalidRentalDurationException;
import exceptions.InvalidVehicleIdException;
import exceptions.VehicleNotAvailableException;
import model.RentalRecord;

/**
 * ============================================================================
 * JAVA CONCEPT 3: MULTITHREADING
 * ============================================================================
 * RentalProcessingThread represents a background worker thread responsible for
 * processing an individual customer's rental transaction.
 * 
 * Why Multithreading here?
 * - In a real enterprise system, hundreds of customers may attempt to book
 *   vehicles at the exact same instant from web or mobile terminals.
 * - By offloading the transaction to a separate thread, the main UI thread
 *   does not freeze.
 * - When multiple threads attempt to book the SAME vehicle simultaneously,
 *   thread synchronization in RentalService ensures only one succeeds,
 *   preventing race conditions and double-booking.
 */
public class RentalProcessingThread implements Runnable {

    private final RentalService rentalService;
    private final String customerName;
    private final String vehicleId;
    private final int rentalDays;

    private RentalRecord resultRecord;
    private Exception failureException;
    private boolean successful;

    public RentalProcessingThread(RentalService rentalService, String customerName, String vehicleId, int rentalDays) {
        this.rentalService = rentalService;
        this.customerName = customerName;
        this.vehicleId = vehicleId;
        this.rentalDays = rentalDays;
        this.successful = false;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.printf("[%s] [START] Processing rental request for '%s' (Vehicle ID: %s, Days: %d)...%n",
                threadName, customerName, vehicleId, rentalDays);

        try {
            // Simulate realistic processing overhead (e.g., identity verification, payment processing)
            Thread.sleep(600);

            // Execute the synchronized rental method
            resultRecord = rentalService.processRental(customerName, vehicleId, rentalDays);
            successful = true;

            System.out.printf("[%s] [SUCCESS] Booking confirmed for '%s'! Record ID: %s, Total: $%.2f%n",
                    threadName, customerName, resultRecord.getRentalId(), resultRecord.getTotalCost());

        } catch (InterruptedException e) {
            System.out.printf("[%s] [INTERRUPTED] Rental processing thread was interrupted: %s%n",
                    threadName, e.getMessage());
            this.failureException = e;
            Thread.currentThread().interrupt(); // Restore interrupted status
        } catch (VehicleNotAvailableException e) {
            // This occurs when another thread booked the vehicle first (Synchronization in action!)
            System.out.printf("[%s] [DENIED - DOUBLE BOOKING PREVENTED] %s%n",
                    threadName, e.getMessage());
            this.failureException = e;
        } catch (InvalidVehicleIdException | InvalidRentalDurationException e) {
            System.out.printf("[%s] [ERROR] Validation failure: %s%n",
                    threadName, e.getMessage());
            this.failureException = e;
        } catch (Exception e) {
            System.out.printf("[%s] [UNEXPECTED ERROR] %s%n",
                    threadName, e.getMessage());
            this.failureException = e;
        } finally {
            System.out.printf("[%s] [END] Transaction processing concluded for '%s'.%n",
                    threadName, customerName);
        }
    }

    public RentalRecord getResultRecord() {
        return resultRecord;
    }

    public Exception getFailureException() {
        return failureException;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
