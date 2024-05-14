package com.vietanhcoder.svnhotel.service;

import com.vietanhcoder.svnhotel.model.BookedRoom;
import com.vietanhcoder.svnhotel.repository.BookedRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingRoomService implements IBookingRoomService {

    private final BookedRoomRepository bookedRoomRepository;

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookedRoomRepository.findByRoomId(roomId);
    }
}
