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

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(history);
    }
}

class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void printAllBookings() {
        List<Reservation> list = bookingHistory.getAllReservations();
        for (Reservation r : list) {
            System.out.println(r.getReservationId() + " | " + r.getGuestName() + " | " + r.getRoomType());
        }
    }

    public void printSummary() {
        Map<String, Integer> countMap = new HashMap<>();
        List<Reservation> list = bookingHistory.getAllReservations();

        for (Reservation r : list) {
            String type = r.getRoomType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        for (String type : countMap.keySet()) {
            System.out.println(type + " -> " + countMap.get(type));
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("R001", "Alice", "Single"));
        history.addReservation(new Reservation("R002", "Bob", "Suite"));
        history.addReservation(new Reservation("R003", "Charlie", "Single"));

        BookingReportService reportService = new BookingReportService(history);

        reportService.printAllBookings();
        System.out.println("Summary:");
        reportService.printSummary();
    }
}