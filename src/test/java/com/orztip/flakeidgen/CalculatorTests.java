package com.orztip.flakeidgen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.orztip.flakeidgen.junit.ProjectTestExtension;


@ExtendWith(ProjectTestExtension.class)
public class CalculatorTests {
	
	@Test
	@DisplayName("1 + 1 = 2")
	void addsTwoNumbers() {
		Calculator calculator = new Calculator();
		System.out.println("CalculatorTests.addsTwoNumbers running");
		assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
	}
	
}
