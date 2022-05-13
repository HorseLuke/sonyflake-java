package com.orztip.sonyflake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.orztip.sonyflake.junit.ProjectTestExtension;


@ExtendWith(ProjectTestExtension.class)
public class SlotBitTests {
	
	@Test
	void testBitCalc() {
		
		long a = -1 ^  (-1 << 16);
		assertEquals(65535, a);
		
	}
	
	
	@Test
	void testBitCalc2() {
		
		int mapIndexTotal = 255;
		
		assertEquals(0, 0 & mapIndexTotal);
		assertEquals(1, 1 & mapIndexTotal);
		assertEquals(255, 255 & mapIndexTotal);
		assertEquals(0, 256 & mapIndexTotal);
		assertEquals(1, 257 & mapIndexTotal);

		long test1 = (2147483647L << 4) + 0xF;
		int val = (int) (test1 & mapIndexTotal);
		
		assertEquals(255, val);

		
	}
	
	
}
