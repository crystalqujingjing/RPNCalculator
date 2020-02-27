import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class DivideDecimal extends BinaryOperator<BigDecimal>{
	@Override
	public BigDecimal cal(BigDecimal a, BigDecimal b) {
		return b.divide(a, new MathContext(15, RoundingMode.HALF_UP));
	}
}
