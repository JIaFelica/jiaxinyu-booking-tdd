import java.util.List;

interface IRoomRepository {
    Room findById(String id);
    void save(Room room);
    List<Room> findAll();
}