import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {
    @Test
    void endBeforeStartIsRejected() {
        LocalDateTime ten = LocalDateTime.of(2027, 1, 11, 10, 0);
        LocalDateTime nine = LocalDateTime.of(2027, 1, 11, 9, 0);
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot(ten, nine));
    }

    @Test
    void endSameAsStartIsRejected() {
        LocalDateTime ten = LocalDateTime.of(2027, 1, 11, 10, 0);
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot(ten, ten));
    }

    @Test
    void nullStartIsRejected() {
        LocalDateTime ten = LocalDateTime.of(2027, 1, 11, 10, 0);
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot(null, ten));
    }

    @Test
    void nullEndIsRejected() {
        LocalDateTime ten = LocalDateTime.of(2027, 1, 11, 10, 0);
        assertThrows(IllegalArgumentException.class, () -> new TimeSlot(ten, null));
    }

    @Test
    void durationIsComputed() {
        TimeSlot t = new TimeSlot(LocalDateTime.of(2027, 1, 11, 10, 0),
                                  LocalDateTime.of(2027, 1, 11, 11, 30));
        assertEquals(java.time.Duration.ofMinutes(90), t.duration());
    }

    @Test
    void gettersReturnStoredTimes() {
        TimeSlot t = new TimeSlot(LocalDateTime.of(2027, 1, 11, 10, 0),
                                  LocalDateTime.of(2027, 1, 11, 11, 0));
        assertEquals(LocalDateTime.of(2027, 1, 11, 10, 0), t.getStart());
        assertEquals(LocalDateTime.of(2027, 1, 11, 11, 0), t.getEnd());
    }
}