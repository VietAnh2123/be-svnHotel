package com.vietanhcoder.svnhotel;

import com.vietanhcoder.svnhotel.service.IRoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SvnhotelApplicationTests {

	@Autowired
	private IRoomService roomService;

	@Test
	void contextLoads() {

		List<String> list = roomService.getAllRoomTypes();

		for (String roomType : list) {
			System.out.println(roomType);
		}
	}

}
