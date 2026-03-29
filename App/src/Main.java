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
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, availability.getOrDefault(type, 0) + count);
    }

    public synchronized boolean allocate(String type) {
        int count = availability.getOrDefault(type, 0);
        if (count <= 0) {
            return false;
        }
        availability.put(type, count - 1);
        return true;
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue queue;
    private InventoryService inventory;

    public ConcurrentBookingProcessor(BookingRequestQueue queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation r = queue.getRequest();
            if (r == null) {
                break;
            }

            synchronized (inventory) {
                boolean success = inventory.allocate(r.getRoomType());
                if (success) {
                    String roomId = r.getRoomType() + "-" + UUID.randomUUID().toString().substring(0, 5);
                    System.out.println(Thread.currentThread().getName() + " confirmed " + r.getGuestName() + " -> " + roomId);
                } else {
                    System.out.println(Thread.currentThread().getName() + " failed " + r.getGuestName());
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BookingRequestQueue queue = new BookingRequestQueue();
        InventoryService inventory = new InventoryService();

        inventory.addRoom("Single", 2);

        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Single"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory), "T1");
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory), "T2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}