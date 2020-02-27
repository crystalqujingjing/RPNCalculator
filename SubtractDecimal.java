import java.math.BigDecimal;

public class SubtractDecimal extends BinaryOperator<BigDecimal>{
	@Override
	public BigDecimal cal(BigDecimal a, BigDecimal b) {
		return b.subtract(a);
	}
}
