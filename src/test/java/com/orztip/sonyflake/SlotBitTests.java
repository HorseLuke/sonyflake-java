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
	
}
