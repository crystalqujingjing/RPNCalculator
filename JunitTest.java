import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class JUnitTest {
	
	@Test
	public void testAdd() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = "7";
		assertEquals(c.display(c.calculate("2 5 +")), exp);
	}
	
	@Test
	public void testSubtract() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = "7.2";
		assertEquals(c.display(c.calculate("7.3 0.1 -")), exp);
	}
	
	@Test
	public void testMultiply() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = "-100";
		assertEquals(c.display(c.calculate("-5 20 *")), exp);
	}
	
	@Test
	public void testDivide() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = "0.3333333333";
		assertEquals(c.display(c.calculate("1 3 /")), exp);
	}
	
	@Test
	public void testSqrt() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = new BigDecimal(Math.sqrt(2)).setScale(10, RoundingMode.HALF_UP).toString();// Set the result to 10 decimal places
		assertEquals(c.display(c.calculate("2 sqrt")), exp);		
	}
	
	@Test
	public void testUndo() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = "20 5";
		assertEquals(c.display(c.calculate("5 4 3 2 undo undo * 5 * undo")), exp);
	}
	
	@Test
	public void testClear() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = "";
		assertEquals(c.display(c.calculate("5 2 - clear")), exp);
	}
	
	@Test
	public void testInputTwice() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp1 = "10.4";
		assertEquals(c.display(c.calculate("5.2 2 *")), exp1);
		String exp2 = "11.4";
		assertEquals(c.display(c.calculate("1 +")), exp2);
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testInsufficientParams() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = "operator * (position: 15): insufficient parameters";
		thrown.expect(ValidationException.class);  
	    thrown.expectMessage(exp);  
		c.calculate("1 2 3 * 5 + * * 6 5");
	}
	
	@Test
	public void testWrongOperator() throws Exception {
		RPNDecimalCalculator c = new RPNDecimalCalculator();
		String exp = "operator mod is not a valid operator";
		thrown.expect(ValidationException.class);
		thrown.expectMessage(exp);
		c.calculate("1 2 3 mod");
	}
	
}
