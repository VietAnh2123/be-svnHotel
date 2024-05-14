package com.vietanhcoder.svnhotel.controller;

import com.vietanhcoder.svnhotel.exception.PhotoRetrievingExeption;
import com.vietanhcoder.svnhotel.exception.ResourceNotFoundExeption;
import com.vietanhcoder.svnhotel.model.BookedRoom;
import com.vietanhcoder.svnhotel.model.Room;
import com.vietanhcoder.svnhotel.response.BookingResponse;
import com.vietanhcoder.svnhotel.response.RoomResponse;
import com.vietanhcoder.svnhotel.service.IBookingRoomService;
import com.vietanhcoder.svnhotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final IRoomService roomService;
    private final IBookingRoomService bookingRoomService;

    @PostMapping(value = "/add/room")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {

       List<Room> rooms = roomService.getRooms();
       List<RoomResponse> responses = new ArrayList<>();

       for (Room room : rooms) {
           byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
           if(photoBytes != null && photoBytes.length > 0) {
               String base64photo = Base64.encodeBase64String(photoBytes);
               RoomResponse response = getRoomResponse(room);
               response.setPhoto(base64photo);
               responses.add(response);
           }
       }
       return ResponseEntity.ok(responses);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
        Optional<Room> room = roomService.getRoomById(roomId);

        return room.map(theRoom -> {
            RoomResponse response = getRoomResponse(theRoom);
            return ResponseEntity.ok(Optional.of(response));
        }).orElseThrow(() -> new ResourceNotFoundExeption("Room not found"));
    }

    private RoomResponse getRoomResponse(Room room){
        List<BookedRoom> bookings = getAllBookingRoomsByRoomId(room.getId());
//        List<BookingResponse> bookingInfo = bookings.stream().map(bookedRoom ->
//                new BookingResponse(bookedRoom.getBookingId(),
//                        bookedRoom.getCheckInDate(),
//                        bookedRoom.getCheckOutDate(),
//                        bookedRoom.getBookingConfirmationCode()))
//                .toList();
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if(photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1,(int) photoBlob.length());
            }catch (SQLException e){
                throw new PhotoRetrievingExeption("Error getting photo bytes");
            }
        }
        return new RoomResponse(room.getId(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.isBooked(),
                photoBytes);
    }

    private List<BookedRoom> getAllBookingRoomsByRoomId(Long roomId){
        return bookingRoomService.getAllBookingsByRoomId(roomId);
    }

    @GetMapping("/get/types")
    public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        theRoom.setPhoto(photoBlob);

        RoomResponse response = getRoomResponse(theRoom);

        return ResponseEntity.ok(response);
    }
}
