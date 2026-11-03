package model;

/**
 * ============================================================================
 * JAVA CONCEPT 2: PACKAGES & INHERITANCE (Subclass Car)
 * ============================================================================
 * Car is a specialized subclass extending Vehicle.
 * It inherits all attributes and methods of Vehicle while introducing
 * car-specific features: seatingCapacity and carType.
 */
public class Car extends Vehicle {

    private int seatingCapacity;
    private String carType; // e.g., Sedan, SUV, Sports, Hatchback

    /**
     * Parameterized Constructor for Car.
     */
    public Car(String vehicleId, String brand, String model, double rentalPricePerDay,
               int seatingCapacity, String carType) {
        super(vehicleId, brand, model, rentalPricePerDay);
        this.seatingCapacity = seatingCapacity;
        this.carType = carType;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    /**
     * Concrete implementation of abstract method displayDetails() from Vehicle.
     */
    @Override
    public void displayDetails() {
        System.out.printf("| %-8s | %-5s | %-12s | %-14s | %-10s | %-12s | $%-9.2f | %-11s |%n",
                getVehicleId(),
                "Car",
                getBrand(),
                getModel(),
                carType,
                seatingCapacity + " Seats",
                getRentalPricePerDay(),
                (isAvailable() ? "AVAILABLE" : "RENTED"));
    }
}
