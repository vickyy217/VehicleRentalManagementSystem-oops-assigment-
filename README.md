# 🚗 Vehicle Rental Management System

A complete, beginner-friendly, and robust Java console application developed for a college programming assignment. The application demonstrates core Object-Oriented Programming (OOP) and advanced Java concepts without relying on any external third-party libraries.

---

## 📌 Table of Contents
1. [Core Java Concepts Demonstrated](#-core-java-concepts-demonstrated)
   - [1. Interfaces](#1-interfaces)
   - [2. Packages](#2-packages)
   - [3. Multithreading & Synchronization](#3-multithreading--synchronization)
   - [4. Exception Handling](#4-exception-handling)
2. [Project Folder Structure](#-project-folder-structure)
3. [How to Compile and Run](#-how-to-compile-and-run)
   - [Prerequisites](#prerequisites)
   - [Option A: Standard Terminal / Command Prompt](#option-a-standard-terminal--command-prompt)
   - [Option B: Using Provided JDK 26 Path](#option-b-using-provided-jdk-26-path)
   - [Option C: Dedicated Standalone Concurrency Demo](#option-c-dedicated-standalone-concurrency-demo)
4. [Application Features & Menu Guide](#-application-features--menu-guide)
5. [Sample Preloaded Vehicles](#-sample-preloaded-vehicles)
6. [Testing & Verification Scenarios](#-testing--verification-scenarios)

---

## 🎓 Core Java Concepts Demonstrated

This project is structured specifically to showcase the four required Java concepts in depth:

### 1. Interfaces
- **File**: `src/model/Rentable.java`
- **What it is**: An interface defines an abstract contract of public method signatures that implementing classes must fulfill.
- **Implementation**:
  - `Rentable` defines four core rental operations:
    - `void rent(int days) throws VehicleNotAvailableException, InvalidRentalDurationException;`
    - `void returnVehicle();`
    - `double calculateRentalCost(int days);`
    - `boolean isAvailable();`
  - The abstract class `Vehicle` implements `Rentable`.
  - Concrete subclasses `Car` and `Bike` inherit this contract, demonstrating **Polymorphism** and **Abstraction**. Any rentable asset in the business can be referenced via the `Rentable` interface type.

### 2. Packages
- **Directory Layout**:
  - `model`: Holds domain entities (`Rentable`, `Vehicle`, `Car`, `Bike`, `RentalRecord`).
  - `service`: Encapsulates business logic, data persistence, and thread execution (`RentalService`, `RentalProcessingThread`).
  - `exceptions`: Custom application exceptions (`VehicleNotAvailableException`, `InvalidVehicleIdException`, `InvalidRentalDurationException`).
  - `main`: Console user interface and application entry points (`Main`, `ConcurrencyDemo`).
- **Encapsulation & Access Modifiers**:
  - Variables are declared `private` to protect internal state.
  - Public getters and setters provide controlled mutation and access.
  - Package structure enforces separation of concerns (Model-Service-UI architecture).

### 3. Multithreading & Synchronization
- **Files**: `src/service/RentalProcessingThread.java`, `src/service/RentalService.java`, `src/main/ConcurrencyDemo.java`
- **What it is**: Multithreading allows concurrent execution of tasks on independent threads of execution.
- **Implementation**:
  - **Worker Thread**: `RentalProcessingThread` implements `java.lang.Runnable`. When a user requests to rent a vehicle, a dedicated background thread is spawned to process the transaction asynchronously.
  - **Thread Methods Used**:
    - `Thread thread = new Thread(task, "Thread-Name");`
    - `thread.start()`: Begins asynchronous execution.
    - `Thread.sleep(ms)`: Simulates real-world database/payment processing delays.
    - `thread.join()`: Suspends the calling thread until the worker thread terminates.
    - `Thread.currentThread().getName()`: Identifies the active thread.
  - **Synchronization & Race Condition Prevention**:
    - In `RentalService.processRental()`, critical sections are protected with `synchronized (vehicle) { ... }`.
    - If multiple customer threads attempt to rent the same vehicle at the exact same millisecond, the vehicle object acts as an exclusive lock (Mutex).
    - The first thread acquires the lock, verifies availability, and marks the vehicle rented.
    - Subsequent threads waiting on the lock enter the block, detect that `vehicle.isAvailable()` is now `false`, and throw `VehicleNotAvailableException`.
    - **Result**: Double-booking is safely and completely prevented!

### 4. Exception Handling
- **Files**:
  - `src/exceptions/VehicleNotAvailableException.java` (Checked exception when a requested vehicle is already rented)
  - `src/exceptions/InvalidVehicleIdException.java` (Checked exception when an entered vehicle ID does not exist)
  - `src/exceptions/InvalidRentalDurationException.java` (Checked exception when duration is $\le 0$)
- **Try-Catch-Finally Blocks**:
  - Handled extensively across `Main.java` and `RentalService.java`.
  - Non-integer input errors in menu and day prompts (`NumberFormatException`) are intercepted to prevent program crashes.
  - `finally` blocks are implemented to ensure transaction lifecycle cleanup and notification output execute unconditionally.

---

## 📁 Project Folder Structure

```
VehicleRentalSystem/
│
├── src/
│   ├── exceptions/
│   │   ├── InvalidRentalDurationException.java   # Exception for invalid days (<= 0)
│   │   ├── InvalidVehicleIdException.java        # Exception for non-existent vehicle IDs
│   │   └── VehicleNotAvailableException.java     # Exception when vehicle is already booked
│   │
│   ├── model/
│   │   ├── Rentable.java                         # [INTERFACE] Defines rental contract
│   │   ├── Vehicle.java                          # [ABSTRACT CLASS] Base class implementing Rentable
│   │   ├── Car.java                              # [SUBCLASS] Extends Vehicle (seats, car type)
│   │   ├── Bike.java                             # [SUBCLASS] Extends Vehicle (engine cc, bike type)
│   │   └── RentalRecord.java                     # Stores transaction receipt data
│   │
│   ├── service/
│   │   ├── RentalProcessingThread.java           # [MULTITHREADING] Runnable worker task
│   │   └── RentalService.java                    # [SERVICE & SYNCHRONIZATION] Fleet & lock management
│   │
│   └── main/
│       ├── Main.java                             # [ENTRY POINT] Interactive Menu console UI
│       └── ConcurrencyDemo.java                  # [TEST SUITE] Automated multithreading race test
│
├── bin/                                          # Compiled .class bytecode (packaged by namespace)
│   ├── exceptions/
│   ├── main/
│   ├── model/
│   └── service/
│
└── README.md                                     # Project documentation
```

---

## 🚀 How to Compile and Run

### Prerequisites
- Java Development Kit (JDK 8 or higher, e.g. JDK 8, 11, 17, 21, or 26).
- Standard terminal (PowerShell, Command Prompt, or Bash).

---

### Option A: Standard Terminal / Command Prompt
If `javac` and `java` are configured in your system `PATH`:

1. Open a terminal and navigate to the project directory:
   ```powershell
   cd "c:\Users\shash\OneDrive\Documents\college shit\codes\java\VehicleRentalSystem"
   ```

2. Compile all source files into the `bin` directory:
   ```powershell
   javac -d bin src/exceptions/*.java src/model/*.java src/service/*.java src/main/*.java
   ```

3. Run the interactive console application:
   ```powershell
   java -cp bin main.Main
   ```

---

### Option B: Using Provided JDK 26 Path
If `javac` is located in the local college directory (`../oracleJdk-26`):

1. Compile targeting compatibility:
   ```powershell
   & "..\oracleJdk-26\bin\javac.exe" --release 8 -d bin (Get-ChildItem -Path src -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)
   ```

2. Run the application:
   ```powershell
   java -cp bin main.Main
   ```

---

### Option C: Dedicated Standalone Concurrency Demo
To run the automated multithreading test directly (simulating 4 concurrent customer threads competing for the same car without needing manual menu input):

```powershell
java -cp bin main.ConcurrencyDemo
```

---

## 🖥 Application Features & Menu Guide

When you launch `Main`, the application presents an interactive menu:

```
===============================================================================
            WELCOME TO THE VEHICLE RENTAL MANAGEMENT SYSTEM                   
   Demonstrating: Interfaces | Packages | Multithreading | Exception Handling   
===============================================================================
-------------------------------------------------------------------------------
                                  MAIN MENU                                    
-------------------------------------------------------------------------------
  1. Display Available Vehicles
  2. Rent a Vehicle (Processed on a separate worker thread)
  3. Return a Vehicle
  4. View Rental Details
  5. Exit
-------------------------------------------------------------------------------
  6. [DEMO] Run Multithreading Concurrency Test (Simulate Race Condition)
-------------------------------------------------------------------------------
Enter your choice (1-5, or 6): 
```

### Menu Actions:
1. **Display Available Vehicles**: Formats and prints a clean tabular view of all cars and motorcycles that are currently free to rent.
2. **Rent a Vehicle**:
   - Prompts for Customer Name, Vehicle ID, and Duration in Days.
   - Spawns a dedicated worker thread (`RentalProcessingThread`).
   - Uses `thread.join()` to await transaction processing.
   - Generates a full itemized receipt with total charges.
   - Triggers `VehicleNotAvailableException`, `InvalidVehicleIdException`, or `InvalidRentalDurationException` on bad inputs.
3. **Return a Vehicle**:
   - Prompts for Vehicle ID.
   - Restores vehicle status to `AVAILABLE` in the fleet inventory.
   - Updates the associated transaction record to `RETURNED`.
4. **View Rental Details**:
   - Displays all historical and active transactions with Receipt IDs, customer names, vehicles, dates, and amounts.
5. **Exit**:
   - Exits cleanly and releases resources.
6. **[Bonus Demo] Run Multithreading Concurrency Test**:
   - Simulates 4 concurrent customer threads competing for the exact same vehicle simultaneously to visually demonstrate synchronization preventing double booking.

---

## 📋 Sample Preloaded Vehicles

| Vehicle ID | Category | Brand | Model | Specification | Daily Rate | Status |
|:---:|:---:|:---|:---|:---|:---:|:---:|
| `CAR-101` | Car | Toyota | Camry | Sedan, 5 Seats | \$55.00 | AVAILABLE |
| `CAR-102` | Car | Honda | CR-V | SUV, 7 Seats | \$75.00 | AVAILABLE |
| `CAR-103` | Car | Ford | Mustang | Sports, 4 Seats | \$120.00 | AVAILABLE |
| `CAR-104` | Car | Tesla | Model 3 | Electric, 5 Seats | \$95.00 | AVAILABLE |
| `BIK-201` | Bike | Yamaha | MT-07 | Naked Sport, 689 cc | \$35.00 | AVAILABLE |
| `BIK-202` | Bike | Royal Enfield | Classic 350 | Cruiser, 349 cc | \$25.00 | AVAILABLE |
| `BIK-203` | Bike | Kawasaki | Ninja 400 | SuperSport, 399 cc | \$45.00 | AVAILABLE |
| `BIK-204` | Bike | Harley-Davidson | Iron 883 | Cruiser, 883 cc | \$60.00 | AVAILABLE |

---

## 🧪 Testing & Verification Scenarios

| Test Case | User Input | Expected Behavior | Java Concept Verified |
|:---|:---|:---|:---|
| **View Fleet** | Option `1` | Displays all 8 preloaded vehicles with rates and specifications. | Packages, Encapsulation |
| **Valid Rental** | Option `2` $\rightarrow$ "Alice", `CAR-101`, `3` days | Worker thread processes booking. Receipt generated for \$165.00. Status becomes RENTED. | Multithreading (`Thread`, `Runnable`, `join()`) |
| **Double Booking** | Option `2` $\rightarrow$ "Bob", `CAR-101`, `2` days | Caught `VehicleNotAvailableException`: Vehicle is already rented. | Exception Handling & Thread Safety |
| **Invalid ID** | Option `2` $\rightarrow$ "Charlie", `XYZ-999`, `2` days | Caught `InvalidVehicleIdException`: Vehicle with ID 'XYZ-999' does not exist. | Exception Handling |
| **Invalid Duration** | Option `2` $\rightarrow$ "David", `BIK-201`, `-4` days | Caught `InvalidRentalDurationException`: Rental duration must be at least 1 day. | Exception Handling |
| **Return Vehicle** | Option `3` $\rightarrow$ `CAR-101` | Restores `CAR-101` to AVAILABLE. Audit record updated to RETURNED. | Interface method `returnVehicle()` |
| **Concurrent Race Condition** | Option `6` or `ConcurrencyDemo` | 4 simultaneous threads attempt to book `CAR-102`. Exactly ONE succeeds; 3 are rejected. | Multithreading & `synchronized` Mutex Locks |

---

## 📄 License & Academic Integrity
This project is authored as an educational submission for college coursework in Advanced Java Programming. All code adheres to standard Java development conventions and OOP design principles.
