import java.util.LinkedList;

public abstract class UnaryOperator<T> extends Operator<T>{
	@Override
	public void process(LinkedList<T> stack, String str, int pos) throws ValidationException {
		if (stack.isEmpty()) {
			String errorMessage = String.format("operator %s (position: %s): insufficient parameters", str, pos);
			throw new ValidationException(errorMessage);
		}
		else {
			stack.push(cal(stack.pop()));
		}
	}
	
	public abstract T cal(T a);
}
