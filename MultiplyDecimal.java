import java.math.BigDecimal;

public class MultiplyDecimal extends BinaryOperator<BigDecimal>{
	@Override
	public BigDecimal cal(BigDecimal a, BigDecimal b) {
		return a.multiply(b);
	}
}
