package com.orztip.sonyflake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.Arrays;

import com.orztip.sonyflake.junit.ProjectTestExtension;


@ExtendWith(ProjectTestExtension.class)
public class SonyflakePropertiesConfigTests {
	
	@Test
	void testDefaultConstructor() {
		
		SonyflakeProperties prop = new SonyflakeProperties();
		
		int[] config = prop.getBitAllocationConfig();
		
		int[] expectedConfig =  {39, 8, 16};
		
		assertEquals(true, Arrays.equals(config, expectedConfig));

		long[] maxNumber = prop.getBitAllocationMaxNumber();
		
		long[] expectedMaxNumber =  {549755813887L, 255, 65535};
		
		assertEquals(true, Arrays.equals(expectedMaxNumber, maxNumber));

	}

	/**
	 * 单机提升到最大1638300 QPS，但横向机器最大数缩小到twitter snowflake的1024
	 */
	@Test
	void testDefaultConstructorWithMoreSequenceBit() {
		
		SonyflakeProperties prop = new SonyflakeProperties(14, 10);
		
		int[] config = prop.getBitAllocationConfig();
		
		int[] expectedConfig =  {39, 14, 10};
		
		assertEquals(Arrays.toString(expectedConfig), Arrays.toString(config));

		long[] maxNumber = prop.getBitAllocationMaxNumber();
		
		long[] expectedMaxNumber =  {549755813887L, 16383, 1023};
		
		assertEquals(Arrays.toString(expectedMaxNumber), Arrays.toString(maxNumber));

	}
	
	@Test
	void testThrowException() {
		
		assertThrowsExactly(IllegalArgumentException.class, () -> {
			new SonyflakeProperties(14, 11);
		}, "Param lengthSequenceBit + lengthMachineIdBit  should be equal to 24");
		
		assertThrowsExactly(IllegalArgumentException.class, () -> {
			new SonyflakeProperties(-1, 25);
		}, "Param lengthSequenceBit should be greater than 0");
		
		assertThrowsExactly(IllegalArgumentException.class, () -> {
			new SonyflakeProperties(24, 0);
		}, "Param lengthMachineIdBit should be greater than 0");
		
	}
	
	
}
