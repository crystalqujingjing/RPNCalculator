import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Operator {

	public static BigDecimal add(BigDecimal a, BigDecimal b) { return a.add(b); }
		
	public static BigDecimal subtract(BigDecimal a, BigDecimal b) { return a.subtract(b); }
	
	public static BigDecimal multiply(BigDecimal a, BigDecimal b) { return a.multiply(b); }
	
	public static BigDecimal divide(BigDecimal a, BigDecimal b) { return a.divide(b, new MathContext(15, RoundingMode.HALF_UP)); }

	public static BigDecimal sqrt(BigDecimal a, int precision) {
		// Use Newton's method to get the square root
		// (x+a/x)/2 where x is the initial value for iteration
	    MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
	    BigDecimal deviation = a;
	    int cnt = 0;
	    while (cnt < 100) {
	        deviation = (deviation.add(a.divide(deviation, mc))).divide(BigDecimal.valueOf(2), mc);
	        cnt++;
	    }
	    deviation = deviation.setScale(precision, BigDecimal.ROUND_HALF_UP);
	    return deviation;
	}
}
