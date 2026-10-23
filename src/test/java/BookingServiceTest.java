import org.junit.jupiter.api.*;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {
    private static final Clock CLOCK =
        Clock.fixed(Instant.parse("2027-01-11T09:00:00Z"), ZoneOffset.UTC);
    private BookingService service;

    @BeforeEach
    void setUp() {
        MemoryRoomRepository rooms = new MemoryRoomRepository();
        rooms.save(new Room("R1", "Room 1"));
        rooms.save(new Room("R2", "Room 2"));
        service = new BookingService(rooms, new MemoryBookingRepository(), CLOCK);
    }

    private static TimeSlot slot(int startHour, int endHour) {
        return new TimeSlot(LocalDateTime.of(2027, 1, 11, startHour, 0),
                            LocalDateTime.of(2027, 1, 11, endHour, 0));
    }

    @Test
    void bookingAFreeRoomReturnsTheBooking() {
        Booking booking = service.book("alice", "R1", slot(10, 11));
        assertEquals(new Booking("alice", "R1", slot(10, 11)), booking);
    }

    @Test
    void bookingNonExistentRoomIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.book("alice", "R9", slot(10, 11)));
    }

    @Test
    void bookingSlotNotInFutureIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.book("alice", "R1", slot(8, 9)));
    }

    @Test
    void bookingSlotLongerThanMaxSlotIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.book("alice", "R1", slot(10, 14)));
    }

    @Test
    void rejectsOverlappingBookingsOnSameRoom() {
        service.book("alice", "R1", slot(10, 12));
        assertThrows(IllegalStateException.class, () -> service.book("bob", "R1", slot(11, 13)));
    }

    @Test
    void rejectsOverlappingBookingsBySameUser() {
        service.book("alice", "R1", slot(10, 12));
        assertThrows(IllegalStateException.class, () -> service.book("alice", "R2", slot(11, 13)));
    }

    @Test
    void cancellingNonExistentBookingIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.cancel("alice", "R1", slot(10, 11)));
    }

    @Test
    void cancellingFreesTheSlot() {
        service.book("alice", "R1", slot(10, 11));
        service.cancel("alice", "R1", slot(10, 11));
        Booking again = service.book("alice", "R1", slot(10, 11));
        assertEquals(new Booking("alice", "R1", slot(10, 11)), again);
    }

    @Test
    void bookingsForAreInStartOrder() {
        service.book("alice", "R1", slot(11, 12));
        service.book("alice", "R2", slot(10, 11));
        List<Booking> bookings = service.bookingsFor("alice");
        assertEquals(new Booking("alice", "R2", slot(10, 11)), bookings.get(0));
        assertEquals(new Booking("alice", "R1", slot(11, 12)), bookings.get(1));
    }

    @Test
    void bookingExactlyMaxSlotIsAllowed() {
        Booking booking = service.book("alice", "R1", slot(10, 13));
        assertEquals(new Booking("alice", "R1", slot(10, 13)), booking);
    }

    @Test
    void touchingSlotsOnSameRoomAreAllowed() {
        service.book("alice", "R1", slot(10, 11));
        Booking second = service.book("bob", "R1", slot(11, 12));
        assertEquals(new Booking("bob", "R1", slot(11, 12)), second);
    }

    @Test
    void sameUserCannotBookTwoRoomsForSameTime() {
        service.book("alice", "R1", slot(10, 11));
        assertThrows(IllegalStateException.class, () -> service.book("alice", "R2", slot(10, 11)));
    }

    @Test
    void rejectsAFourthActiveBookingForSameUser() {
        service.book("alice", "R1", slot(10, 11));
        service.book("alice", "R1", slot(12, 13));
        service.book("alice", "R1", slot(14, 15));
        assertThrows(IllegalStateException.class, () -> service.book("alice", "R1", slot(16, 17)));
    }
}
