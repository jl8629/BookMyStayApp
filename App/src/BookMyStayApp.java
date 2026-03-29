import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class InventoryService implements Serializable {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, availability.getOrDefault(type, 0) + count);
    }

    public Map<String, Integer> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<String, Integer> availability) {
        this.availability = availability;
    }
}

class BookingHistory implements Serializable {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }

    public void setAll(List<Reservation> list) {
        this.history = list;
    }
}

class SystemState implements Serializable {
    private InventoryService inventory;
    private BookingHistory history;

    public SystemState(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public InventoryService getInventory() {
        return inventory;
    }

    public BookingHistory getHistory() {
        return history;
    }
}

class PersistenceService {
    private String fileName = "system_state.dat";

    public void save(SystemState state) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(state);
            oos.close();
            System.out.println("State saved");
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public SystemState load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            SystemState state = (SystemState) ois.readObject();
            ois.close();
            System.out.println("State loaded");
            return state;
        } catch (Exception e) {
            System.out.println("Load failed, starting fresh");
            return new SystemState(new InventoryService(), new BookingHistory());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        PersistenceService persistence = new PersistenceService();

        SystemState state = persistence.load();

        InventoryService inventory = state.getInventory();
        BookingHistory history = state.getHistory();

        inventory.addRoom("Single", 2);
        history.addReservation(new Reservation("R001", "Alice", "Single"));

        persistence.save(new SystemState(inventory, history));

        SystemState restored = persistence.load();

        System.out.println("Inventory: " + restored.getInventory().getAvailability());
        for (Reservation r : restored.getHistory().getAll()) {
            System.out.println(r.getReservationId() + " " + r.getGuestName() + " " + r.getRoomType());
        }
    }
}