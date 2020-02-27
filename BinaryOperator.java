import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class BinaryOperator<T> extends Operator<T>{
	@Override
	public void process(LinkedList<T> stack, String str, int pos) throws ValidationException {
		List<T> twoNums = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			if (stack.isEmpty()) {
				String errorMessage = String.format("operator %s (position: %s): insufficient parameters", str, pos);
				throw new ValidationException(errorMessage);
			}
			else {
				twoNums.add(stack.pop());
			}
		}
		stack.push(cal(twoNums.get(0), twoNums.get(1)));
	}
	
	public abstract T cal(T a, T b);
}
