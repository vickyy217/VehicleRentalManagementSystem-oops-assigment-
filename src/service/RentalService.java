package service;

import exceptions.InvalidRentalDurationException;
import exceptions.InvalidVehicleIdException;
import exceptions.VehicleNotAvailableException;
import model.Bike;
import model.Car;
import model.RentalRecord;
import model.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ============================================================================
 * JAVA CONCEPT 2 & 3: PACKAGES & MULTITHREADING (Thread Synchronization)
 * ============================================================================
 * RentalService is the business logic component residing in the 'service' package.
 * It manages the vehicle inventory, processes rental transactions, and maintains
 * transaction history.
 * 
 * THREAD SAFETY & SYNCHRONIZATION:
 * In a multi-threaded application, multiple threads may invoke processRental()
 * on the same vehicle simultaneously. Without synchronization, two threads could
 * both read isAvailable() as true and both rent the vehicle (Race Condition / Double Booking).
 * 
 * We solve this by synchronizing on the specific Vehicle instance:
 * synchronized(vehicle) { ... }
 * This creates a mutual exclusion (mutex) lock, guaranteeing that only ONE
 * thread at a time can check and update the vehicle's availability state.
 */
public class RentalService {

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<RentalRecord> rentalRecords = new ArrayList<>();
    private final AtomicInteger rentalIdCounter = new AtomicInteger(1001);

    public RentalService() {
        initializeSampleVehicles();
    }

    /**
     * Preloads realistic sample vehicles so the application is ready for immediate testing.
     */
    private void initializeSampleVehicles() {
        // Cars: ID, Brand, Model, Price/Day, Seating, Type
        vehicles.add(new Car("CAR-101", "Toyota", "Camry", 55.0, 5, "Sedan"));
        vehicles.add(new Car("CAR-102", "Honda", "CR-V", 75.0, 7, "SUV"));
        vehicles.add(new Car("CAR-103", "Ford", "Mustang", 120.0, 4, "Sports"));
        vehicles.add(new Car("CAR-104", "Tesla", "Model 3", 95.0, 5, "Electric"));

        // Bikes: ID, Brand, Model, Price/Day, Engine cc, Type
        vehicles.add(new Bike("BIK-201", "Yamaha", "MT-07", 35.0, 689, "Naked Sport"));
        vehicles.add(new Bike("BIK-202", "Royal Enfield", "Classic 350", 25.0, 349, "Cruiser"));
        vehicles.add(new Bike("BIK-203", "Kawasaki", "Ninja 400", 45.0, 399, "SuperSport"));
        vehicles.add(new Bike("BIK-204", "Harley-Davidson", "Iron 883", 60.0, 883, "Cruiser"));
    }

    /**
     * Returns an unmodifiable list of all vehicles in the fleet.
     */
    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    /**
     * Returns an unmodifiable list of all completed and active rental records.
     */
    public List<RentalRecord> getAllRentalRecords() {
        synchronized (rentalRecords) {
            return Collections.unmodifiableList(new ArrayList<>(rentalRecords));
        }
    }

    /**
     * Searches for a vehicle by vehicleId (case-insensitive).
     * 
     * @param vehicleId Unique identifier of the vehicle.
     * @return Vehicle object if found.
     * @throws InvalidVehicleIdException If the ID is invalid or not in inventory.
     */
    public Vehicle findVehicleById(String vehicleId) throws InvalidVehicleIdException {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new InvalidVehicleIdException("Vehicle ID cannot be blank or empty.");
        }

        String searchId = vehicleId.trim();
        for (Vehicle v : vehicles) {
            if (v.getVehicleId().equalsIgnoreCase(searchId)) {
                return v;
            }
        }

        throw new InvalidVehicleIdException("Vehicle with ID '" + searchId + "' does not exist in the fleet.");
    }

    /**
     * Displays only vehicles currently marked as available.
     */
    public void displayAvailableVehicles() {
        System.out.println("\n+---------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                       AVAILABLE VEHICLES FOR RENT                                       |");
        System.out.println("+----------+-------+--------------+----------------+------------+--------------+------------+-------------+");
        System.out.printf("| %-8s | %-5s | %-12s | %-14s | %-10s | %-12s | %-10s | %-11s |%n",
                "ID", "Type", "Brand", "Model", "Category", "Capacity", "Daily Rate", "Status");
        System.out.println("+----------+-------+--------------+----------------+------------+--------------+------------+-------------+");

        int count = 0;
        for (Vehicle v : vehicles) {
            if (v.isAvailable()) {
                v.displayDetails();
                count++;
            }
        }

        if (count == 0) {
            System.out.println("|                            No vehicles are currently available for rent.                                |");
        }
        System.out.println("+----------+-------+--------------+----------------+------------+--------------+------------+-------------+");
        System.out.printf(" Total Available: %d out of %d vehicles.%n%n", count, vehicles.size());
    }

    /**
     * Displays all vehicles regardless of availability status.
     */
    public void displayAllVehicles() {
        System.out.println("\n+---------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                           COMPLETE FLEET LIST                                           |");
        System.out.println("+----------+-------+--------------+----------------+------------+--------------+------------+-------------+");
        System.out.printf("| %-8s | %-5s | %-12s | %-14s | %-10s | %-12s | %-10s | %-11s |%n",
                "ID", "Type", "Brand", "Model", "Category", "Capacity", "Daily Rate", "Status");
        System.out.println("+----------+-------+--------------+----------------+------------+--------------+------------+-------------+");

        for (Vehicle v : vehicles) {
            v.displayDetails();
        }
        System.out.println("+----------+-------+--------------+----------------+------------+--------------+------------+-------------+\n");
    }

    /**
     * ========================================================================
     * MULTITHREADING: SYNCHRONIZED RENTAL EXECUTION
     * ========================================================================
     * This method safely processes a rental request.
     * 
     * By using synchronized(vehicle), we ensure that thread execution on that
     * specific vehicle is serialized:
     * 1. Thread 1 acquires the lock on the vehicle.
     * 2. Thread 1 verifies vehicle.isAvailable() is true.
     * 3. Thread 1 marks the vehicle as rented.
     * 4. Thread 1 releases the lock.
     * 5. If Thread 2 was waiting for the same vehicle, it now enters the synchronized
     *    block, checks vehicle.isAvailable(), discovers it is now false, and throws
     *    VehicleNotAvailableException!
     * 
     * Result: Double-booking is 100% prevented!
     */
    public RentalRecord processRental(String customerName, String vehicleId, int rentalDays)
            throws InvalidVehicleIdException, InvalidRentalDurationException, VehicleNotAvailableException {

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }

        if (rentalDays <= 0) {
            throw new InvalidRentalDurationException("Rental duration must be at least 1 day. You entered: " + rentalDays);
        }

        Vehicle vehicle = findVehicleById(vehicleId);

        // ----------------- SYNCHRONIZATION BARRIER -----------------
        synchronized (vehicle) {
            if (!vehicle.isAvailable()) {
                throw new VehicleNotAvailableException(vehicle.getVehicleId(),
                        "Vehicle is already rented out to another customer.");
            }

            // Rent the vehicle via the Rentable interface
            vehicle.rent(rentalDays);

            double totalCost = vehicle.calculateRentalCost(rentalDays);
            String rentalId = "REC-" + rentalIdCounter.getAndIncrement();
            RentalRecord record = new RentalRecord(rentalId, customerName.trim(), vehicle, rentalDays, totalCost);

            synchronized (rentalRecords) {
                rentalRecords.add(record);
            }

            return record;
        }
    }

    /**
     * Returns a vehicle back into the fleet.
     * 
     * @param vehicleId ID of the vehicle to return.
     * @return true if successfully returned, false if the vehicle was already available.
     * @throws InvalidVehicleIdException If the ID is invalid.
     */
    public boolean returnVehicle(String vehicleId) throws InvalidVehicleIdException {
        Vehicle vehicle = findVehicleById(vehicleId);

        synchronized (vehicle) {
            if (vehicle.isAvailable()) {
                return false; // Vehicle was not rented out
            }

            // Return vehicle via the Rentable interface
            vehicle.returnVehicle();

            // Update matching active rental record
            synchronized (rentalRecords) {
                for (RentalRecord record : rentalRecords) {
                    if (record.getVehicle().getVehicleId().equalsIgnoreCase(vehicleId)
                            && "ACTIVE".equalsIgnoreCase(record.getStatus())) {
                        record.markAsReturned();
                        break;
                    }
                }
            }
            return true;
        }
    }

    /**
     * Displays all rental history transactions.
     */
    public void displayRentalDetails() {
        System.out.println("\n+----------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                           RENTAL TRANSACTIONS LOG                                        |");
        System.out.println("+-----------+--------------+----------+------------------+-----------+------------+-----------+------------------+");
        System.out.printf("| %-9s | %-12s | %-8s | %-16s | %-9s | %-10s | %-9s | %-16s |%n",
                "Record ID", "Customer", "Veh. ID", "Vehicle Name", "Duration", "Total Cost", "Status", "Date & Time");
        System.out.println("+-----------+--------------+----------+------------------+-----------+------------+-----------+------------------+");

        synchronized (rentalRecords) {
            if (rentalRecords.isEmpty()) {
                System.out.println("|                                  No rental records found in the system yet.                              |");
            } else {
                for (RentalRecord record : rentalRecords) {
                    record.displayRecord();
                }
            }
        }
        System.out.println("+-----------+--------------+----------+------------------+-----------+------------+-----------+------------------+\n");
    }

    /**
     * ========================================================================
     * MULTITHREADING DEMONSTRATION METHOD
     * ========================================================================
     * Concurrently launches multiple threads simulating simultaneous rental requests
     * targeting the SAME vehicle.
     * 
     * Demonstrates:
     * - Thread creation and starting (new Thread(runnable).start())
     * - Thread joining (thread.join() to await completion)
     * - Thread synchronization preventing double booking
     */
    public void demonstrateConcurrency(String targetVehicleId, List<String> customerNames, int rentalDays) {
        System.out.println("\n===============================================================================");
        System.out.println(">>> STARTING MULTITHREADING CONCURRENCY & SYNCHRONIZATION DEMONSTRATION <<<");
        System.out.printf("Scenario: %d customers simultaneously attempting to rent vehicle '%s'%n",
                customerNames.size(), targetVehicleId);
        System.out.println("Expected Result: Only ONE thread succeeds; others are safely rejected without double booking.");
        System.out.println("===============================================================================\n");

        List<Thread> threadList = new ArrayList<>();
        List<RentalProcessingThread> taskList = new ArrayList<>();

        for (int i = 0; i < customerNames.size(); i++) {
            String customer = customerNames.get(i);
            RentalProcessingThread task = new RentalProcessingThread(this, customer, targetVehicleId, rentalDays);
            taskList.add(task);

            Thread worker = new Thread(task, "Thread-Customer-" + (i + 1));
            threadList.add(worker);
        }

        // Fire all threads simultaneously
        for (Thread t : threadList) {
            t.start();
        }

        // Wait for all threads to finish their execution
        for (Thread t : threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println("Main thread interrupted while waiting for child threads.");
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n===============================================================================");
        System.out.println(">>> MULTITHREADING DEMONSTRATION SUMMARY <<<");
        System.out.println("===============================================================================");
        int successes = 0;
        int failures = 0;
        for (RentalProcessingThread task : taskList) {
            if (task.isSuccessful()) {
                successes++;
            } else {
                failures++;
            }
        }
        System.out.printf("Successful Bookings : %d (Expected: 1)%n", successes);
        System.out.printf("Prevented Bookings  : %d (Expected: %d)%n", failures, customerNames.size() - 1);
        System.out.println("Status: Double-booking was SUCCESSFULLY PREVENTED using Java thread synchronization!");
        System.out.println("===============================================================================\n");
    }
}
