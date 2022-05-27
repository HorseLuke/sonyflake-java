package com.orztip.sonyflake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.orztip.sonyflake.junit.ProjectTestExtension;


@ExtendWith(ProjectTestExtension.class)
public class IdGeneratorTests {
	
	@Test
	void testDefault() {
				
		IdGenerator sf = new IdGenerator();
		
		IdGeneratorProperties prop = sf.getProp();
		
		int[] config = prop.getBitAllocationConfig();
		
		int[] expectedConfig =  {39, 8, 16};
		
		Assertions.assertEquals(Arrays.toString(expectedConfig), Arrays.toString(config));

		long[] maxNumber = prop.getBitAllocationMaxNumber();
		
		long[] expectedMaxNumber =  {549755813887L, 255, 65535};
		
		Assertions.assertEquals(Arrays.toString(expectedMaxNumber), Arrays.toString(maxNumber));
		
		HashSet<Long> data = new HashSet<Long>();
		for(int i = 1; i <= 255; i++) {
			try {
				long id = sf.nextId();
				if(data.contains(id)) {
					Assertions.fail("duplicated id generated");
				}
				data.add(id);
			} catch (RuntimeException e) {
				Assertions.fail(e);
			}
		}
		
	}

	@Test
	void testExceedCounter() {
				
		IdGenerator sf = new IdGenerator();
		
		HashSet<Long> data = new HashSet<Long>();
		for(int i = 1; i <= 2048; i++) {
			try {
				long id = sf.nextId();
				if(data.contains(id)) {
					Assertions.fail("duplicated id generated");
				}
				data.add(id);
			} catch (RuntimeException e) {
				Assertions.fail(e);
			}
		}
		
	}
	

	@Test
	void testExceedCounterThrowException() {
		
		
		IdGeneratorProperties prop = new IdGeneratorProperties();
		prop.setWaitForNextTimeBitSlotIfUnusual(false);
		
		IdGenerator sf = new IdGenerator(prop);
		
		HashSet<Long> data = new HashSet<Long>();
		for(int i = 1; i <= 2048; i++) {
			try {
				long id = sf.nextId();
				if(data.contains(id)) {
					Assertions.fail("duplicated id generated");
				}
				data.add(id);
			} catch (RuntimeException e) {
				if(e.getMessage() == "CAN_NOT_GENERATE_NEXT_ID_BY_SEQUENCE_FULL") {
					return ;
				}
				Assertions.fail(e);
			}
		}
		
		Assertions.fail("Does not throw expected exception 'CAN_NOT_GENERATE_NEXT_ID_BY_SEQUENCE_FULL'");
		
	}
	
	
}
