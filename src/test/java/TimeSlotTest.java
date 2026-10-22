import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {
    private static TimeSlot slot(int startHour, int endHour) {
        return new TimeSlot(LocalDateTime.of(2027, 1, 11, startHour, 0),
                            LocalDateTime.of(2027, 1, 11, endHour, 0));
    }

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

    @ParameterizedTest(name = "{0}-{1} vs {2}-{3} overlaps: {4}")
    @CsvSource({
        "10, 11, 11, 12, false",
        "10, 12, 11, 13, true",
        "10, 11, 12, 13, false",
        "10, 14, 11, 12, true",
        "10, 12, 10, 12, true",
        "12, 13, 10, 11, false"
    })
    void overlapCases(int aStart, int aEnd, int bStart, int bEnd, boolean expected) {
        assertEquals(expected, slot(aStart, aEnd).overlaps(slot(bStart, bEnd)));
        assertEquals(expected, slot(bStart, bEnd).overlaps(slot(aStart, aEnd)));
    }

    @Test
    void equalSlotsAreEqualAndSameHashCode() {
        TimeSlot a = slot(10, 12);
        TimeSlot b = slot(10, 12);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentSlotsAreNotEqual() {
        assertNotEquals(slot(10, 11), slot(11, 12));
    }
}