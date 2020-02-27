import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class SqrtDecimal extends UnaryOperator<BigDecimal>{
	@Override
	public BigDecimal cal(BigDecimal a) {
		// Use Newton's method to get the square root
				// (x+a/x)/2 where x is the initial value for iteration
			    MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
			    BigDecimal deviation = a;
			    int cnt = 0;
			    while (cnt < 100) {
			        deviation = (deviation.add(a.divide(deviation, mc))).divide(BigDecimal.valueOf(2), mc);
			        cnt++;
			    }
			    deviation = deviation.setScale(15, BigDecimal.ROUND_HALF_UP);
			    return deviation;
	}
}
