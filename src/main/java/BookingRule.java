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
