package com.vietanhcoder.svnhotel.service;

import com.vietanhcoder.svnhotel.model.BookedRoom;

import java.util.List;

public interface IBookingRoomService {

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    List<BookedRoom> getAllBookings();

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    void cancelBooking(Long bookingId);
}
