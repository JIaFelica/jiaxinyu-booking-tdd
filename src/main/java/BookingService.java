import java.time.*;
import java.util.*;

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
        checkRoomExists(roomId);
        checkSlotInFuture(slot);
        checkSlotLength(slot);
        checkRoomFree(roomId, slot);
        checkUserFree(userId, slot);
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

    private void checkRoomExists(String roomId) {
        if (rooms.findById(roomId) == null) {
            throw new IllegalArgumentException("Room does not exist");
        }
    }

    private void checkSlotInFuture(TimeSlot slot) {
        if (!slot.getStart().isAfter(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("Slot must be in the future");
        }
    }

    private void checkSlotLength(TimeSlot slot) {
        if (slot.duration().compareTo(MAX_SLOT) > 0) {
            throw new IllegalArgumentException("Slot is longer than MAX_SLOT");
        }
    }

    private void checkRoomFree(String roomId, TimeSlot slot) {
        boolean roomOverlap = bookings.findByRoom(roomId).stream()
            .anyMatch(b -> b.getSlot().overlaps(slot));
        if (roomOverlap) {
            throw new IllegalStateException("Room is already booked for this time");
        }
    }

    private void checkUserFree(String userId, TimeSlot slot) {
        boolean userOverlap = bookings.findByUser(userId).stream()
            .anyMatch(b -> b.getSlot().overlaps(slot));
        if (userOverlap) {
            throw new IllegalStateException("User already has a booking at this time");
        }
    }
}
