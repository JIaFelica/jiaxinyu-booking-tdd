import java.time.*;
import java.util.*;

public class BookingService {
    public static final Duration MAX_SLOT = Duration.ofHours(3);

    private final IRoomRepository rooms;
    private final IBookingRepository bookings;
    private final Clock clock;
    private final List<BookingRule> rules;

    public BookingService(IRoomRepository rooms, IBookingRepository bookings, Clock clock) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.clock = clock;
        this.rules = List.of(
            new RoomMustExist(rooms),
            new SlotInFuture(clock),
            new WithinMaxSlot(),
            new RoomFree(bookings),
            new UserFree(bookings)
        );
    }

    public Booking book(String userId, String roomId, TimeSlot slot) {
        for (BookingRule rule : rules) {
            rule.check(userId, roomId, slot);
        }
        Booking b = new Booking(userId, roomId, slot);
        bookings.save(b);
        return b;
    }

    public void cancel(String userId, String roomId, TimeSlot slot) {
        Booking toCancel = bookings.findByRoom(roomId).stream()
            .filter(b -> b.getUserId().equals(userId) && b.getSlot().equals(slot))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Booking does not exist"));
        bookings.delete(toCancel);
    }

    public List<Booking> bookingsFor(String userId) {
        return bookings.findByUser(userId).stream()
            .sorted(Comparator.comparing(b -> b.getSlot().getStart()))
            .toList();
    }
}
