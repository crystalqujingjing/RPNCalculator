import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Pattern;

@FunctionalInterface
interface BinaryOperation {
	BigDecimal operate(BigDecimal a, BigDecimal b);
}

public class RPNCalculator {
	LinkedList<BigDecimal> stack;
	LinkedList<LinkedList<BigDecimal>> prevStack;// keep track of each operation's result
	Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$"); // pattern for a real number
	String input;
	
	public RPNCalculator(String s) {
		stack = new LinkedList<>();
		prevStack = new LinkedList<>();
		input = s + " ";// Add white space padding to input string
	}
	
	private void calculate(String s) throws ValidationException {
		int pointer = 0;
		int prev = 0;
		while (pointer < s.length()) {
			if (s.charAt(pointer) == ' ') {
				String str = s.substring(prev, pointer);
				// Determine real number or an operator
				if (pattern.matcher(str).matches()) {
					stack.push(new BigDecimal(str));
					prevStack.push(deepCopy(stack));
				} else {
					switch (str) {
					case "+":
						process(str, prev + 1, (a, b) -> Operator.add(a, b));
						break;
					case "-": 
						process(str, prev + 1, (a, b) -> Operator.subtract(a, b));
						break;
					case "*":
						process(str, prev + 1, (a, b) -> Operator.multiply(a, b));
						break;
					case "/":
						process(str, prev + 1, (a, b) -> Operator.divide(a, b));
						break;
					case "sqrt": 
						if (stack.isEmpty()) {
							String errorMessage = "operator" + str + "(position: " + prev +"): insufficient parameters";
							throw new ValidationException(errorMessage);
						}
						else {
							stack.push(Operator.sqrt(stack.pop(), 15));// 15 decimal places of precision
						}
						break;
					case "undo": undo(); break;
					case "clear": clear(); break;
					default:
						String errorMessage = String.format("operator %s is not a valid operator", str);
						throw new ValidationException(errorMessage);
					}
				}
				prev = pointer + 1;
			}
			pointer++;
		}
	}
	
	private void process(String str, int pos, BinaryOperation op) throws ValidationException{
		BigDecimal[] twoNums = new BigDecimal[2];
		for (int i = 0; i < 2; i++) {
			if (stack.isEmpty()) {
				String errorMessage = String.format("operator %s (position: %s): insufficient parameters", str, pos);
				throw new ValidationException(errorMessage);
			}
			else {
				twoNums[i] = stack.pop();
			}
		}
		stack.push(op.operate(twoNums[1], twoNums[0]));
		prevStack.push(deepCopy(stack));
	}
	
	private LinkedList<BigDecimal> deepCopy(LinkedList<BigDecimal> stack) {
		LinkedList<BigDecimal> copy = new LinkedList<>();
		ListIterator<BigDecimal> iter = stack.listIterator(0);
		while (iter.hasNext()) {
			copy.push(iter.next());
		}
		return copy;
	}
	
	private void undo() {
		if (prevStack.isEmpty()) return;
		prevStack.pop();
		if (prevStack.isEmpty()) stack = new LinkedList<>();
		else stack = deepCopy(prevStack.peek());
	}
	
	private void clear() {
		stack = new LinkedList<>();
		prevStack.push(new LinkedList<>());
	}
	
	public String display() {
		try {
			calculate(input);
		} catch (ValidationException e) {
			return e.getMessage();
		}
		String res = new String();
		for (int i = stack.size() - 1; i >= 0; i--) {
			res += stack.get(i).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + " ";
		}
		// Remove trailing empty space
		if (res.length() == 0) return res;
		return res.substring(0, res.length() - 1);
	}
	
}
