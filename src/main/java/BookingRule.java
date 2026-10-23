import java.time.*;

interface BookingRule {
    void check(String userId, String roomId, TimeSlot slot);
}

class RoomMustExist implements BookingRule {
    private final IRoomRepository rooms;

    RoomMustExist(IRoomRepository rooms) { this.rooms = rooms; }

    public void check(String userId, String roomId, TimeSlot slot) {
        if (rooms.findById(roomId) == null) {
            throw new IllegalArgumentException("Room does not exist");
        }
    }
}

class SlotInFuture implements BookingRule {
    private final Clock clock;

    SlotInFuture(Clock clock) { this.clock = clock; }

    public void check(String userId, String roomId, TimeSlot slot) {
        if (!slot.getStart().isAfter(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("Slot must be in the future");
        }
    }
}

class WithinMaxSlot implements BookingRule {
    public void check(String userId, String roomId, TimeSlot slot) {
        if (slot.duration().compareTo(BookingService.MAX_SLOT) > 0) {
            throw new IllegalArgumentException("Slot is longer than MAX_SLOT");
        }
    }
}

class RoomFree implements BookingRule {
    private final IBookingRepository bookings;

    RoomFree(IBookingRepository bookings) { this.bookings = bookings; }

    public void check(String userId, String roomId, TimeSlot slot) {
        boolean roomOverlap = bookings.findByRoom(roomId).stream()
            .anyMatch(b -> b.getSlot().overlaps(slot));
        if (roomOverlap) {
            throw new IllegalStateException("Room is already booked for this time");
        }
    }
}

class UserFree implements BookingRule {
    private final IBookingRepository bookings;

    UserFree(IBookingRepository bookings) { this.bookings = bookings; }

    public void check(String userId, String roomId, TimeSlot slot) {
        boolean userOverlap = bookings.findByUser(userId).stream()
            .anyMatch(b -> b.getSlot().overlaps(slot));
        if (userOverlap) {
            throw new IllegalStateException("User already has a booking at this time");
        }
    }
}
