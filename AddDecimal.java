import java.math.BigDecimal;

public class AddDecimal extends BinaryOperator<BigDecimal>{
	@Override
	public BigDecimal cal(BigDecimal a, BigDecimal b) {
		return a.add(b);
	}
}
