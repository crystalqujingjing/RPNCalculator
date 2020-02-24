import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class JUnitTest {
	
	@Test
	public void testAdd() {
		RPNCalculator c = new RPNCalculator("2 5 +");
		String exp = "7";
		assertEquals(c.display(), exp);
	}
	
	@Test
	public void testSubtract() {
		RPNCalculator c = new RPNCalculator("7.3 0.1 -");
		String exp = "7.2";
		assertEquals(c.display(), exp);
	}
	
	@Test
	public void testMultiply() {
		RPNCalculator c = new RPNCalculator("-5 20 *");
		String exp = "-100";
		assertEquals(c.display(), exp);
	}
	
	@Test
	public void testDivide() {
		RPNCalculator c = new RPNCalculator("1 3 /");
		String exp = "0.3333333333";
		assertEquals(c.display(), exp);
	}
	
	@Test
	public void testSqrt() {
		RPNCalculator c = new RPNCalculator("2 sqrt");
		String exp = new BigDecimal(Math.sqrt(2)).setScale(10, RoundingMode.HALF_UP).toString();// Set the result to 10 decimal places
		assertEquals(c.display(), exp);		
	}
	
	@Test
	public void testUndo() {
		RPNCalculator c = new RPNCalculator("5 4 3 2 undo undo * 5 * undo");
		String exp = "20 5";
		assertEquals(c.display(), exp);
	}
	
	@Test
	public void testClear() {
		RPNCalculator c = new RPNCalculator("5 2 - clear");
		String exp = "";
		assertEquals(c.display(), exp);
	}
	
	@Test
	public void testInsufficientParams() {
		RPNCalculator c = new RPNCalculator("1 2 3 * 5 + * * 6 5");
		String exp = "operator * (position: 15): insufficient parameters";
		assertEquals(c.display(), exp);
	}
	
	@Test
	public void testWrongOperator() {
		RPNCalculator c = new RPNCalculator("1 2 3 mod");
		String exp = "operator mod is not a valid operator";
		assertEquals(c.display(), exp);
	}
}
