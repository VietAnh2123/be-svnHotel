package com.vietanhcoder.svnhotel.controller;

import com.vietanhcoder.svnhotel.exception.InvalidBookingRequestException;
import com.vietanhcoder.svnhotel.exception.ResourceNotFoundExeption;
import com.vietanhcoder.svnhotel.model.BookedRoom;
import com.vietanhcoder.svnhotel.model.Room;
import com.vietanhcoder.svnhotel.response.BookingResponse;
import com.vietanhcoder.svnhotel.response.RoomResponse;
import com.vietanhcoder.svnhotel.service.IBookingRoomService;
import com.vietanhcoder.svnhotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingRoomController {

    private final IBookingRoomService bookingRoomService;
    private final IRoomService roomService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookingRoomService.getAllBookings();

        List<BookingResponse> bookingResponses = new ArrayList<>();

        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }

        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try {
            BookedRoom booking = bookingRoomService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);

            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundExeption e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/rooms/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,@RequestBody BookedRoom bookingRequest){
        try {
            String confirmationCode = bookingRoomService.saveBooking(roomId, bookingRequest);

            return ResponseEntity.ok("Room booked successfully ! Your confirmation code is: " + confirmationCode);
        }catch (InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){

        bookingRoomService.cancelBooking(bookingId);

        return ResponseEntity.noContent().build();
    }

    private BookingResponse getBookingResponse(BookedRoom booking){
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse();
        room.setId(theRoom.getId());
        room.setRoomType(theRoom.getRoomType());
        room.setRoomPrice(theRoom.getRoomPrice());

        BookingResponse bookingResponse = new BookingResponse(
                booking.getBookingId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestFullname(),
                booking.getGuestEmail(),
                booking.getNumberOfAdults(),
                booking.getNumberOfChildren(),
                booking.getTotalNumOfGuest(),
                booking.getBookingConfirmationCode(),
                room);

        return bookingResponse;
    }
}
