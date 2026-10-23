import java.time.*;
import java.util.List;

public class BookingService {
    public static final Duration MAX_SLOT = Duration.ofHours(3);

    private final IRoomRepository rooms;
    private final IBookingRepository bookings;
    private final Clock clock;

    public BookingService(IRoomRepository rooms, IBookingRepository bookings, Clock clock) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.clock = clock;
    }

    public Booking book(String userId, String roomId, TimeSlot slot) {
        if (rooms.findById(roomId) == null) {
            throw new IllegalArgumentException("Room does not exist");
        }
        if (!slot.getStart().isAfter(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("Slot must be in the future");
        }
        if (slot.duration().compareTo(MAX_SLOT) > 0) {
            throw new IllegalArgumentException("Slot is longer than MAX_SLOT");
        }
        boolean roomOverlap = bookings.findByRoom(roomId).stream()
            .anyMatch(b -> b.getSlot().overlaps(slot));
        if (roomOverlap) {
            throw new IllegalStateException("Room is already booked for this time");
        }
        Booking b = new Booking(userId, roomId, slot);
        bookings.save(b);
        return b;
    }

    public void cancel(String userId, String roomId, TimeSlot slot) {
        throw new UnsupportedOperationException("TODO: implement using TDD");
    }

    public List<Booking> bookingsFor(String userId) {
        throw new UnsupportedOperationException("TODO: implement using TDD");
    }
}