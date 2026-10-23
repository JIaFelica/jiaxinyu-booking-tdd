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
