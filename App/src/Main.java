import java.util.*;

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

class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public Reservation pollRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
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
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

class BookingService {
    private InventoryService inventoryService;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomAllocations;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
    }

    public void processRequests(BookingRequestQueue queue) {
        while (!queue.isEmpty()) {
            Reservation reservation = queue.pollRequest();
            String type = reservation.getRoomType();

            if (inventoryService.getAvailable(type) > 0) {
                String roomId = generateRoomId(type);

                while (allocatedRoomIds.contains(roomId)) {
                    roomId = generateRoomId(type);
                }

                allocatedRoomIds.add(roomId);
                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);
                inventoryService.decrement(type);

                System.out.println("Confirmed: " + reservation.getGuestName() + " -> " + roomId);
            } else {
                System.out.println("Failed: " + reservation.getGuestName() + " (No availability)");
            }
        }
    }

    private String generateRoomId(String type) {
        return type + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}

public class Main {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Suite", 1);

        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Suite"));

        BookingService bookingService = new BookingService(inventory);
        bookingService.processRequests(queue);
    }
}