package com.vietanhcoder.svnhotel.service;

import com.vietanhcoder.svnhotel.model.BookedRoom;

import java.util.List;

public interface IBookingRoomService {

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

}
