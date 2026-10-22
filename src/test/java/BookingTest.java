import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    @Test
    void bookingStoresUserRoomAndSlot() {
        TimeSlot slot = new TimeSlot(LocalDateTime.of(2027, 1, 11, 10, 0),
                                     LocalDateTime.of(2027, 1, 11, 11, 0));
        Booking b = new Booking("alice", "R1", slot);
        assertEquals("alice", b.getUserId());
        assertEquals("R1", b.getRoomId());
        assertEquals(slot, b.getSlot());
    }

    @Test
    void twoEqualBookingsAreEqual() {
        TimeSlot s = new TimeSlot(LocalDateTime.of(2027, 1, 11, 10, 0),
                                  LocalDateTime.of(2027, 1, 11, 11, 0));
        Booking b1 = new Booking("alice", "R1", s);
        Booking b2 = new Booking("alice", "R1", s);
        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }
}