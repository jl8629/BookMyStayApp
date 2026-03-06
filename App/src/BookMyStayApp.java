import java.util.HashMap;

abstract class Room {
    private String name;
    private int numberOfBeds;
    private double size;
    private double price;

    public Room(String name, int numberOfBeds, double size, double price) {
        this.name = name;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200.0, 100.0);
    }

    @Override
    public void displayDetails() {
        System.out.println(getName() + " - Beds: " + getNumberOfBeds() + ", Size: " + getSize() + " sq ft, Price: $" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350.0, 180.0);
    }

    @Override
    public void displayDetails() {
        System.out.println(getName() + " - Beds: " + getNumberOfBeds() + ", Size: " + getSize() + " sq ft, Price: $" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 400.0);
    }

    @Override
    public void displayDetails() {
        System.out.println(getName() + " - Beds: " + getNumberOfBeds() + ", Size: " + getSize() + " sq ft, Price: $" + getPrice());
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int newAvailability) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newAvailability);
        }
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " - Availability: " + inventory.get(roomType));
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single.getName(), 5);
        inventory.addRoomType(doubleRoom.getName(), 3);
        inventory.addRoomType(suite.getName(), 2);

        single.displayDetails();
        System.out.println("Availability: " + inventory.getAvailability(single.getName()));

        doubleRoom.displayDetails();
        System.out.println("Availability: " + inventory.getAvailability(doubleRoom.getName()));

        suite.displayDetails();
        System.out.println("Availability: " + inventory.getAvailability(suite.getName()));

        inventory.displayInventory();
    }
}

