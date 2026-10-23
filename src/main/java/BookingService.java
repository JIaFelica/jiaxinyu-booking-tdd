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