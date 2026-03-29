import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
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

    public int getAvailable(String type) {
        return availability.getOrDefault(type, -1);
    }

    public void decrement(String type) throws InvalidBookingException {
        if (!availability.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
        int current = availability.get(type);
        if (current <= 0) {
            throw new InvalidBookingException("No availability for room type: " + type);
        }
        availability.put(type, current - 1);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }
}

class BookingValidator {
    public void validate(Reservation reservation, InventoryService inventory) throws InvalidBookingException {
        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }
        if (reservation.getRoomType() == null || reservation.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected");
        }
        if (inventory.getAvailable(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Selected room type is not available");
        }
    }
}

class BookingService {
    private InventoryService inventory;
    private BookingValidator validator;

    public BookingService(InventoryService inventory, BookingValidator validator) {
        this.inventory = inventory;
        this.validator = validator;
    }

    public void confirmBooking(Reservation reservation) {
        try {
            validator.validate(reservation, inventory);
            inventory.decrement(reservation.getRoomType());
            String roomId = reservation.getRoomType() + "-" + UUID.randomUUID().toString().substring(0, 6);
            System.out.println("Booking confirmed for " + reservation.getGuestName() + " Room ID: " + roomId);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Suite", 0);

        BookingValidator validator = new BookingValidator();
        BookingService service = new BookingService(inventory, validator);

        service.confirmBooking(new Reservation("Alice", "Single"));
        service.confirmBooking(new Reservation("", "Single"));
        service.confirmBooking(new Reservation("Bob", "Suite"));
        service.confirmBooking(new Reservation("Charlie", "Double"));
    }
}