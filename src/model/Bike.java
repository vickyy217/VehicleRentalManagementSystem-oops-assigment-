package model;

/**
 * ============================================================================
 * JAVA CONCEPT 2: PACKAGES & INHERITANCE (Subclass Bike)
 * ============================================================================
 * Bike is a specialized subclass extending Vehicle.
 * It inherits all attributes and methods of Vehicle while introducing
 * motorcycle-specific features: engineCapacityCc and bikeType.
 */
public class Bike extends Vehicle {

    private int engineCapacityCc;
    private String bikeType; // e.g., Cruiser, Sport, Commuter, Adventure

    /**
     * Parameterized Constructor for Bike.
     */
    public Bike(String vehicleId, String brand, String model, double rentalPricePerDay,
                int engineCapacityCc, String bikeType) {
        super(vehicleId, brand, model, rentalPricePerDay);
        this.engineCapacityCc = engineCapacityCc;
        this.bikeType = bikeType;
    }

    public int getEngineCapacityCc() {
        return engineCapacityCc;
    }

    public void setEngineCapacityCc(int engineCapacityCc) {
        this.engineCapacityCc = engineCapacityCc;
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }

    /**
     * Concrete implementation of abstract method displayDetails() from Vehicle.
     */
    @Override
    public void displayDetails() {
        System.out.printf("| %-8s | %-5s | %-12s | %-14s | %-10s | %-12s | $%-9.2f | %-11s |%n",
                getVehicleId(),
                "Bike",
                getBrand(),
                getModel(),
                bikeType,
                engineCapacityCc + " cc",
                getRentalPricePerDay(),
                (isAvailable() ? "AVAILABLE" : "RENTED"));
    }
}
