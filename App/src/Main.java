import java.util.*;

class InvalidCancellationException extends Exception {
    public InvalidCancellationException(String message) {
        super(message);
    }
}

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean active;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.active = true;
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

    public String getRoomId() {
        return roomId;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        this.active = false;
    }
}

class InventoryService {
    private Map<String, Integer> availability;

    public InventoryService() {
        availability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availability.put(type, availability.getOrDefault(type, 0) + count);
    }

    public void increment(String type) {
        availability.put(type, availability.getOrDefault(type, 0) + 1);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }
}

class BookingHistory {
    private Map<String, Reservation> history;

    public BookingHistory() {
        history = new HashMap<>();
    }

    public void addReservation(Reservation reservation) {
        history.put(reservation.getReservationId(), reservation);
    }

    public Reservation getReservation(String id) {
        return history.get(id);
    }
}

class CancellationService {
    private InventoryService inventory;
    private BookingHistory history;
    private Stack<String> releasedRoomStack;

    public CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.releasedRoomStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {
        try {
            Reservation reservation = history.getReservation(reservationId);

            if (reservation == null) {
                throw new InvalidCancellationException("Reservation does not exist");
            }

            if (!reservation.isActive()) {
                throw new InvalidCancellationException("Reservation already cancelled");
            }

            releasedRoomStack.push(reservation.getRoomId());
            inventory.increment(reservation.getRoomType());
            reservation.cancel();

            System.out.println("Cancelled: " + reservationId + " Room Released: " + reservation.getRoomId());
        } catch (InvalidCancellationException e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }

    public void printReleasedRooms() {
        System.out.println("Rollback Stack:");
        for (String id : releasedRoomStack) {
            System.out.println(id);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 1);

        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("R001", "Alice", "Single", "Single-123");
        history.addReservation(r1);

        CancellationService service = new CancellationService(inventory, history);

        service.cancelBooking("R001");
        service.cancelBooking("R001");
        service.cancelBooking("R002");

        service.printReleasedRooms();
    }
}