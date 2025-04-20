import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;


public class Project {
    ArrayList<Vehicles> vehicles = new ArrayList<>();
    ArrayList<Bookings> bookings = new ArrayList<>();
    HashMap<String, Customer> customers = new HashMap<>();
    Customer currentCustomer = null;
    

    // The above line tracks the current customer, it will be null if no
    // customer is logged in

    public static void main(String[] args) {
        Project project = new Project();
        project.initializeData();
        project.start();
    }

    // Initializing data in the program database
    private void initializeData() {
        // Sample customer data for login
        customers.put("admin", new Customer("admin", "admin123"));
        customers.put("Qaail", new Customer("qaail", "qaail123"));

        // Normal classic cars
        ClassicCar car1 = new ClassicCar("1", "Toyota Corolla", 40.0, true, false);
        ClassicCar car2 = new ClassicCar("2", "Honda Civic", 42.0, true, false);
        ClassicCar car3 = new ClassicCar("3", "Hyundai Elantra", 39.0, true, false);
        ClassicCar car4 = new ClassicCar("4", "Nissan Sentra", 38.0, true, false);

        vehicles.add(car1);
        vehicles.add(car2);
        vehicles.add(car3);
        vehicles.add(car4);

        // SUVs
        SUV suv1 = new SUV("5", "Toyota RAV4", 59.0, true, true);
        SUV suv2 = new SUV("6", "Honda CR-V", 60.0, true, true);
        SUV suv3 = new SUV("7", "Ford Explorer", 65.0, true, true);
        SUV suv4 = new SUV("8", "Chevrolet Tahoe", 70.0, true, true);

        vehicles.add(suv1);
        vehicles.add(suv2);
        vehicles.add(suv3);
        vehicles.add(suv4);

        // Electric Vehicles
        EV ev1 = new EV("9", "Tesla Model 3", 120.0, true, 500);
        EV ev2 = new EV("10", "Nissan Leaf", 55.0, true, 240);
        EV ev3 = new EV("11", "BYD Atto 3", 60.0, true, 400);
        EV ev4 = new EV("12", "Ford Mustang Mach-E", 130.0, true, 300);

        vehicles.add(ev1);
        vehicles.add(ev2);
        vehicles.add(ev3);
        vehicles.add(ev4);

        // Super Cars
        SuperCar superCar1 = new SuperCar("13", "Porsche 911 Carrera", 1000.0, true, 293);
        SuperCar superCar2 = new SuperCar("14", "Lamborghini Huracán", 1500.0, true, 325);
        SuperCar superCar3 = new SuperCar("15", "Ferrari 488 GTB", 1800.0, true, 330);
        SuperCar superCar4 = new SuperCar("16", "McLaren 720S", 2000.0, true, 341);

        vehicles.add(superCar1);
        vehicles.add(superCar2);
        vehicles.add(superCar3);
        vehicles.add(superCar4);
    }

    public void start() {
        boolean run = true;
        while (run) {
            displayMenu();
            int choice = getintegerInput("\nSelect an option: ");

            if (currentCustomer == null) {
                if (choice == 1) {
                    login();
                } else if (choice == 2) {
                    register();
                } else if (choice == 3) {
                    run = false;
                    System.out.println("Thank you for using our service. Have a great day!");
                } else {
                    System.out.println("Invalid option.");
                }
            } else {
                if (choice == 1) {
                    rentVehicle();
                } else if (choice == 2) {
                    buyVehicle();
                } else if (choice == 3) {
                    viewallBookings();
                } else if (choice == 4) {
                    logout();
                } else {
                    System.out.println("Invalid option.");
                }
            }
        }
    }

    // Below are specific attributes in the program which will direct user
    // based on specific input provided

    // All data is private so outside classes cannot access
    // Menu to be displayed when program is run if null or not
    private void displayMenu() {
        System.out.println("\n===== AutoNova Online =====");
        if (currentCustomer == null) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
        } else {
            System.out.println("1. Rent a Car");
            System.out.println("2. Buy a Car");
            System.out.println("3. View Bookings and Purchases");
            System.out.println("4. Logout");
        }
    }

    private void login() {
        try {
            System.out.println("Username: ");
            String username = In.nextLine();
            System.out.println("Password: ");
            String password = In.nextLine();

            Customer customer = customers.get(username);
            if (customer != null && customer.getPassword().equals(password)) {
                currentCustomer = customer;
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid Credentials");
            }
        } catch (InputMismatchException e) {
            System.out.println("An error occurred during login. Please try again.");
        }
    }

    // to register a new user in the system
    private void register() {
        System.out.println("Enter a username: ");
        String username = In.nextLine();
        //Check if username already exists
        if (customers.containsKey(username)) {
            System.out.println("Username already exists.\nPlease try registering again.");
            return;
        }
        System.out.println("Enter a password: ");
        String password = In.nextLine();
        customers.put(username, new Customer(username, password));
        System.out.println("Registration successful!");
    }

    // For renting a car based on availability
    private void rentVehicle() {
        System.out.println("\nAvailable Vehicles:");
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicles vehicle = vehicles.get(i);
            if (vehicle.isAvailable()) {
                System.out.println((i + 1) + ": " + vehicle);
            }
        }

        int carNumber = getintegerInput("Select a car(number): ") - 1;
        if (carNumber < 0 || carNumber >= vehicles.size() || !vehicles.get(carNumber).isAvailable()) {
            System.out.println("Invalid selection");
            return;
        }

        Vehicles selectedVehicle = vehicles.get(carNumber);
        int days = getintegerInput("Rental days: ");
        double totalPrice = selectedVehicle.getPricePerDay() * days;
        System.out.println("Total price: $" + totalPrice);

        System.out.println(("Confirm booking? (Y/N): "));
        String confirmation = In.nextLine();
        if (confirmation.equals("Y")) {
            bookings.add(new Bookings(currentCustomer.getUsername(), selectedVehicle.getModel(), days, totalPrice));
            selectedVehicle.setAvailable(false);//Removes vehicle from database
            System.out.println("Booking confirmed!");
        } else {
            System.out.println("Booking cancelled");
        }
    }

    // If customer wants to buy a car
    private void buyVehicle() {
        System.out.println("\nAvailable Vehicles for Purchase:");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicles vehicle = vehicles.get(i);
            if (vehicle.isAvailable()) {
                System.out.println("---------------------------------------");
                System.out.println("Vehicle Number: " + (i + 1));
                System.out.println("Model: " + vehicle.getModel());
                System.out.println("Type: " + vehicle.getType());
                System.out.println("Price: $" + vehicle.getPricePerDay() * 120); // To convert to actual price
                if (vehicle.getType().equals("Classic Car")) {
                    System.out.println("This is a Classic car.");
                } else if (vehicle.getType().equals("Super Car")) {
                    System.out.println("This is a Super car.");
                } else if (vehicle.getType().equals("SUV")) {
                    System.out.println("This is a SUV.");
                } else if (vehicle.getType().equals("EV")) {
                    System.out.println("This is an Electric vehicle.");
                }
                System.out.println("-----------------------------------");
            }
        }
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        int carNumber = getintegerInput("Select a vehicle to buy(number): ") - 1; // Removes a vehicle from database
        if (carNumber < 0 || carNumber >= vehicles.size() || !vehicles.get(carNumber).isAvailable()) {
            System.out.println("Invalid selection.");
            return;
        }

        Vehicles selectVehicles = vehicles.get(carNumber);
        double totalPrice = selectVehicles.getPricePerDay() * 120;

        // Insurance Option Prompt
        System.out.println("Would you like to add insurance? (Y/N): ");
        String insuranceChoice = In.nextLine();
        if (insuranceChoice.equals("Y")) {
            double insuranceFee = selectVehicles.getpriceofInsurance();
            totalPrice += insuranceFee;
            System.out.println("Insurance added for extra $" + insuranceFee);
        } else {
            System.out.println("Proceeding without insurance.");
        }

        // Extended Warranty option
        System.out.println("Would you like to add an extended Warranty? (Y/N): ");
        String warrantyExtension = In.nextLine();
        if (warrantyExtension.equals("Y")) {
            double warrantyFee = 550; // Fixed for any vehicle
            totalPrice += warrantyFee;
            System.out.println("Warranty added for an extra $" + warrantyFee);
        } else {
            System.out.println("No Warranty extension applied");
        }

        // Apply 15% discount if total price is over $10,000
        if (totalPrice > 10000) {
            double discount = totalPrice * 0.15;
            totalPrice -= discount;
            System.out.println("A 15% discount has been applied! Discount amount: $" + discount);
        }   

        // Preset date for the purchase
        String purchaseDate = "2025-04-15";

        // Addition of extra purchases
        System.out.println("Total price: $" + totalPrice);
        System.out.println("Confirm purchase? (Y/N): ");
        if (In.nextLine().equals("Y")) {
            selectVehicles.setAvailable(false);
            bookings.add(new Bookings(currentCustomer.getUsername(), selectVehicles.getModel(), 0, totalPrice));
            System.out.println("Purchase confirmed on " + purchaseDate + "!");
        } else {
            System.out.println("Purchase cancelled.");
        }
    }

    //Ability for user to check purchases and bookings 
    private void viewallBookings(){
        //Display bookings for the logge din customer
        System.out.println(("Bookings for user: ")+currentCustomer.getUsername());
        for(Bookings booking:bookings){
            if(booking.getUserId().equals(currentCustomer.getUsername())){
                //To differentiate between purchased and rented transactions
                String purchaseType;
                if(booking.getDays()>0){
                    purchaseType = "Rented";
                }else{
                    purchaseType = "Purchased";
                }
                System.out.println("Vehicle: "+booking.getVehicleModel()+" | Type: "+purchaseType+" | Total: $" + booking.getTotalPrice());
            }
        }
    }

    //Show summary of purchases when user logs out
    private void logout(){
        System.out.println("\nSummary of Purchases");
        System.out.println("========================================");
        boolean hasPurchases = false;
        for(Bookings booking :bookings){
            if(booking.getUserId().equals(currentCustomer.getUsername())){
                String purchaseType;
                if(booking.getDays()>0){
                    purchaseType = "Rented";
                } else {
                    purchaseType = "Purchased";
                }
                System.out.println("----------------------------------------");
                System.out.println("Car: " + booking.getVehicleModel());
                System.out.println("Type: " + purchaseType);
                System.out.println("Total: $" + booking.getTotalPrice());
                System.out.println("----------------------------------------");
                
                //Incase customer has no transactions
                hasPurchases= true;
            }
        }
       
        if(!hasPurchases){
            System.out.println("No transactions made.");
        }
        System.out.println("========================================");

        System.out.println("Logged out");
        currentCustomer= null;
    }

    //Method for obtaining user input from Customer incase of wrong input
    private int getintegerInput(String enter){
        while(true){
        System.out.println(enter);
        try{
            return In.nextInt();
        } catch (InputMismatchException e){
            System.out.println("Invalid Input,Please enter a number:");
            In.nextLine();//Retry again due to invalid input
        }  
    }
    }         
}








// Parent class for all types of vehicles
class Vehicles {
    private String id; // Vehicle number in the database
    private String model;
    private String type;
    private double pricePerDay;
    private boolean availability;
    private double priceofInsurance;

    // Constructor for the parent class
    public Vehicles(String id, String model, String type, double pricePerDay, boolean availability,
            double priceofInsurance) {
        this.id = id;
        this.model = model;
        this.type = type;
        this.pricePerDay = pricePerDay;
        this.availability = availability;
        this.priceofInsurance = priceofInsurance;
    }

    // Getters for each attribute
    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return availability;
    }

    public double getpriceofInsurance() {
        return priceofInsurance;
    }

    // Setter for availability of that vehicle
    public void setAvailable(boolean availability) {
        this.availability = availability;
    }

    // toString method to be utilized in child classes
    @Override
    public String toString() {
        return getModel() + " [" + getType() + "] : $" + getPricePerDay() + "/day";
    }
}

// ClassicCar class (for regular vehicles)

class ClassicCar extends Vehicles {
    private boolean isHybrid;
    // The above checks if the car is hybrid type or not

    // Constructor
    public ClassicCar(String id, String model, double pricePerDay, boolean availability, boolean isHybrid) {
        super(id, model, "Classic Car", pricePerDay, availability, 300);
        // Initializing attributes to the parent class due to certain attributes being
        // pre defined
        this.isHybrid = isHybrid;
    }

    // Getter for whether car is hybrid or not
    public boolean isHybrid() {
        return isHybrid;
    }

    // toString method for Classic cars
    @Override
    public String toString() {
        String hybridOption;
        if (isHybrid) {
            hybridOption = "Yes";
        } else {
            hybridOption = "No";
        }
        return super.toString() + ", Hybrid Option: " + hybridOption;
    }
}

// SuperCar class for exclusive cars
class SuperCar extends Vehicles {
    private double topSpeed;
    // To indicatee top speed in km/h

    // Constructor
    public SuperCar(String id, String model, double pricePerDay, boolean available, double topSpeed) {
        super(id, model, "Super Car", pricePerDay, available, 1000);

        this.topSpeed = topSpeed;
    }

    // Getter for topSpeed of the Supercar
    public double getTopSpeed() {
        return topSpeed;
    }

    // toString method for Supercar class
    @Override
    public String toString() {
        return super.toString() + ", Top Speed: " + topSpeed + " km/h";
    }
}

// SUV class for all types of SUVs

class SUV extends Vehicles {
    private boolean hasAllWheelDrive;
    // The above ensures if the type of SUV comes with all-wheel drive option

    // Constructor
    public SUV(String id, String model, double pricePerDay, boolean availability, boolean hasAllWheelDrive){
        super(id, model, "SUV", pricePerDay, availability, 600);
    }

    // Getter for All-wheel drive option in SUVs
    public boolean hasAllWheelDrive() {
        return hasAllWheelDrive;
    }

    // toString method for SUVs using parent class
    @Override
    public String toString() {
        String allwheelDrive;
        if (hasAllWheelDrive) {
            allwheelDrive = "Yes";
        } else {
            allwheelDrive = "No";
        }
        return super.toString() + " , All-wheel drive: " + allwheelDrive;
    }

}

// EV Class for all electric vehicles
class EV extends Vehicles {
    private double rangePerCharge;
    // Range in km per one charge

    // Constructor
    public EV(String id, String model, double pricePerDay, boolean availability, double rangePerCharge){
        super(id, model, "EV", pricePerDay, availability, 250);
        // EV will have slightly lower insurance price
        this.rangePerCharge = rangePerCharge;
    }

    // Getter method for range per charge in km
    public double getRangePerCharge() {
        return rangePerCharge;
    }

    // toString method for EV class
    @Override
    public String toString() {
        return super.toString() + ", Range per charge: " + rangePerCharge + " km";
    }
}

// Customer class to store information
class Customer {
    private String username;
    private String password;
    // These attributes ensures security when logging in

    // Constructor
    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters for the attributes
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    // toString method not required here
}

// Bookings class to store all vehicles information
class Bookings {
    private String userId;
    private String vehicleModel;
    private int days;
    private double totalPrice;
    ArrayList<Bookings> listofBookings;
    // Above Arraylist will be an instance variable which will store all bookings
    

    // Constructor
    public Bookings(String userId, String vehicleModel, int days, double totalPrice) {
        this.userId = userId;
        this.vehicleModel = vehicleModel;
        this.days = days;
        this.totalPrice = totalPrice;
    }

    // Constructor to initialize booking list
    public Bookings() {
        this.listofBookings = new ArrayList<>();
    }

    // Method to add a single booking to the list
    public void addBooking(Bookings booking) {
        listofBookings.add(booking);
    }

    // Getters for Bookings class attributes
    public String getUserId() {
        return userId;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Method to display all bookings for a single user
    public void displayBookings(String userId) {
        System.out.println("Bookings for user: " + userId);
        for (Bookings booking : listofBookings) {
            // Use the getUserId() method to access the userId field
            if (booking.getUserId().equals(userId)) {
                System.out.println(booking);
            }
        }
    }

    // toString method to represent a booking
    @Override
    public String toString() {
        return "User: " + userId + " | Vehicle: " + vehicleModel + " | Days: " + days + " | Total: $" + totalPrice;
    }

}
