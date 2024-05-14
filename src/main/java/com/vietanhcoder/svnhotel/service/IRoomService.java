package com.vietanhcoder.svnhotel.service;

import com.vietanhcoder.svnhotel.model.Room;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SQLException, IOException;

    List<Room> getRooms();

    byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException;

    List<String> getAllRoomTypes();

    void deleteRoom(Long roomId);

    Optional<Room> getRoomById(Long roomId);

    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);
}
