
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


class FactorsTester {

	@Test
	void testPerfect1()
	{	
		// TEST 1: should throw the exception because the parameter value is less than 1
		assertThrows(IllegalArgumentException.class, () -> FactorsUtility.perfect(0));
	}
	
	@Test
	void testPerfect2()
	{	
		// TEST 2: should succeed because 1 is a valid parameter value, but is not a perfect number
		assertFalse(FactorsUtility.perfect(1));
	}
	
	@Test
	void testPerfect3()
	{	
		// TEST 3: should succeed because 6 is a valid parameter value, and is a perfect number
		assertTrue(FactorsUtility.perfect(6));
	}
	
	@Test
	void testPerfect4()
	{	
		// TEST 4: should succeed because 7 is a valid parameter value, but is not a perfect number
		// I've coded this using assertEquals to show that there's often more than one way to write a test 
		boolean expected = false;
		assertEquals(expected, FactorsUtility.perfect(7));
	}

//testing getFactors method starts here
	
	@Test
	void testGetFactors1()
	{	
		ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1));
		assertEquals(numbers, FactorsUtility.getFactors(2));
	}
	
	@Test
	void testGetFactors2()
	{	
		ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList());
		assertEquals(numbers, FactorsUtility.getFactors(1));
	}
	
	@Test
	void testGetFactors3()
	{	
		ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList());
		assertEquals(numbers, FactorsUtility.getFactors(0));
	}
	
	@Test
	void testGetFactors4()
	{	
		assertThrows(IllegalArgumentException.class, () -> FactorsUtility.getFactors(-1));
	}
	
	@Test
	void testGetFactors5()
	{	
		ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1,2,3,4,6));
		assertEquals(numbers, FactorsUtility.getFactors(12));
	}
	
//testing factor method starts here
	
	@Test
	void testFactor1() {
		assertThrows(IllegalArgumentException.class, () -> FactorsUtility.factor(-1, 1));
	}
	
	
	@Test
	void testFactor2() {
		assertThrows(IllegalArgumentException.class, () -> FactorsUtility.factor(0, 0));
	}
	
	@Test
	void testFactor3()
	{	
		assertTrue(FactorsUtility.factor(0, 1));
	}
	
	@Test
	void testFactor4()
	{	
		assertFalse(FactorsUtility.factor(1, 2));
	}
	
	@Test
	void testFactor5()
	{	
		assertFalse(FactorsUtility.factor(4, 8));
	}
	

}
