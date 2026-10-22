import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    @Test
    void roomStoresIdAndName() {
        Room r = new Room("R1", "Room 1");
        assertEquals("R1", r.getId());
        assertEquals("Room 1", r.getName());
    }
}
