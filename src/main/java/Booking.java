public class Booking {
    private final String userId;
    private final String roomId;
    private final TimeSlot slot;

    public Booking(String userId, String roomId, TimeSlot slot) {
        this.userId = userId;
        this.roomId = roomId;
        this.slot = slot;
    }

    public String getUserId() { return null; }
    public String getRoomId() { return null; }
    public TimeSlot getSlot() { return null; }
}