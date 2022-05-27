package com.orztip.sonyflake;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.orztip.sonyflake.junit.ProjectTestExtension;


@ExtendWith(ProjectTestExtension.class)
public class IdGeneratorClockBackwardTests {

	@Test
	void testMockClockBackward() {
		
		IdGeneratorClockBackwardLogic sf = new IdGeneratorClockBackwardLogic();
		
		try {
			
			int waitTimeIn10ms = 3;
			
			long backTime = System.currentTimeMillis() / 10L - IdGeneratorClockBackwardLogic.START_TIME + waitTimeIn10ms;
			sf.setCurrentTimeBitSlot(backTime);
			
			long startRunTime = System.currentTimeMillis();
			
			long newTime = sf.doClockBackwardLogic();
			if(newTime <= backTime) {
				Assertions.fail("time not changed");
			}
			
			long processTime = System.currentTimeMillis() - startRunTime;
			
			System.out.println("processed time: " + processTime);
			
			if(processTime < 30) {
				Assertions.fail("time not wait for forward when clock backward, process time: " + processTime);
			}
			
		} catch (Throwable e) {
			Assertions.fail(e);
		}
		
	}

	@Test
	void testMockClockBackwardThrowException() {
		
		IdGeneratorClockBackwardLogic sf = new IdGeneratorClockBackwardLogic();
		sf.setWaitForNextTimeBitSlotIfUnusual(false);
		sf.setCurrentTimeBitSlot(System.currentTimeMillis() / 10L - IdGeneratorClockBackwardLogic.START_TIME + 100);
		
		Assertions.assertThrowsExactly(RuntimeException.class, () -> {
			sf.doClockBackwardLogic();
		});
		
	}
	
}
