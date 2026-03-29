import java.util.*;

class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }
}

class Inventory {
    private Map<String, Integer> availability;

    public Inventory() {
        availability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availability.put(type, availability.getOrDefault(type, 0) + count);
    }

    public int getAvailableCount(String type) {
        return availability.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(availability);
    }
}

class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomMap;

    public SearchService(Inventory inventory, Map<String, Room> roomMap) {
        this.inventory = inventory;
        this.roomMap = roomMap;
    }

    public List<Room> searchAvailableRooms() {
        List<Room> result = new ArrayList<>();
        Map<String, Integer> availability = inventory.getAllAvailability();

        for (String type : availability.keySet()) {
            int count = availability.get(type);
            if (count > 0 && roomMap.containsKey(type)) {
                result.add(roomMap.get(type));
            }
        }
        return result;
    }
}

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();

        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        Map<String, Room> roomMap = new HashMap<>();

        roomMap.put("Single", new Room("Single", 2000, Arrays.asList("WiFi", "AC")));
        roomMap.put("Double", new Room("Double", 3500, Arrays.asList("WiFi", "AC", "TV")));
        roomMap.put("Suite", new Room("Suite", 6000, Arrays.asList("WiFi", "AC", "TV", "Mini Bar")));

        SearchService searchService = new SearchService(inventory, roomMap);

        List<Room> availableRooms = searchService.searchAvailableRooms();

        for (Room room : availableRooms) {
            System.out.println("Type: " + room.getType());
            System.out.println("Price: " + room.getPrice());
            System.out.println("Amenities: " + room.getAmenities());
            System.out.println();
        }
    }
}