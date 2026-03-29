import java.util.*;

class Reservation {
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

class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }
}

public class Main {
    public static void main(String[] args) {
        Reservation reservation = new Reservation("R001", "Alice", "Suite");

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(reservation.getReservationId(), new AddOnService("Breakfast", 500));
        manager.addService(reservation.getReservationId(), new AddOnService("Airport Pickup", 1200));
        manager.addService(reservation.getReservationId(), new AddOnService("Spa", 1500));

        List<AddOnService> services = manager.getServices(reservation.getReservationId());

        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Guest: " + reservation.getGuestName());

        for (AddOnService s : services) {
            System.out.println("Service: " + s.getName() + ", Cost: " + s.getCost());
        }

        double totalCost = manager.calculateTotalCost(reservation.getReservationId());
        System.out.println("Total Add-On Cost: " + totalCost);
    }
}