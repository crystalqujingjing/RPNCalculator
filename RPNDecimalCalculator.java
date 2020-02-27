import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

@FunctionalInterface
interface BinaryOperation {
	BigDecimal operate(BigDecimal a, BigDecimal b);
}

public class RPNDecimalCalculator implements RPNCalculator<BigDecimal>{
	LinkedList<LinkedList<BigDecimal>> prevStack;// keep track of each operation's result
	Map<String, Operator<BigDecimal>> opMap;// map string to operator
	Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$"); // pattern for a real number
	String input;
	
	public RPNDecimalCalculator(String s) {
		prevStack = new LinkedList<>();
		opMap = new HashMap<>();
		loadOperators();
		input = s + " ";// Add white space padding to input string
	}
	
	private void loadOperators() {
		opMap.put("+", new AddDecimal());
		opMap.put("-", new SubtractDecimal());
		opMap.put("*", new MultiplyDecimal());
		opMap.put("/", new DivideDecimal());
		opMap.put("sqrt", new SqrtDecimal());
	}
	
	@Override
	public LinkedList<BigDecimal> calculate(String s) throws ValidationException {
		int pointer = 0;
		int prev = 0;
		LinkedList<BigDecimal> stack = new LinkedList<>();
		while (pointer < s.length()) {
			if (s.charAt(pointer) == ' ') {
				String str = s.substring(prev, pointer);
				// Determine real number or an operator
				if (pattern.matcher(str).matches()) {
					stack.push(new BigDecimal(str));
					prevStack.push(deepCopy(stack));
				} else {
					if (opMap.containsKey(str)) {
						try {
							opMap.get(str).process(stack, str, prev + 1);
						} catch (ValidationException e) {
							throw e;
						}
						prevStack.push(deepCopy(stack));
					} else {
						switch (str) {
						case "undo": stack = undo(stack); break;
						case "clear": stack = clear(stack); break;
						default:
							String errorMessage = String.format("operator %s is not a valid operator", str);
							throw new ValidationException(errorMessage);
						}
					}
				}
				prev = pointer + 1;
			}
			pointer++;
		}
		return stack;
	}
	
	private LinkedList<BigDecimal> deepCopy(LinkedList<BigDecimal> stack) {
		LinkedList<BigDecimal> copy = new LinkedList<>();
		ListIterator<BigDecimal> iter = stack.listIterator(0);
		while (iter.hasNext()) {
			copy.push(iter.next());
		}
		return copy;
	}
	
	private LinkedList<BigDecimal> undo(LinkedList<BigDecimal> stack) {
		if (prevStack.isEmpty()) return new LinkedList<BigDecimal>();
		prevStack.pop();
		if (prevStack.isEmpty()) return new LinkedList<BigDecimal>();
		else return deepCopy(prevStack.peek());
	}
	
	private LinkedList<BigDecimal> clear(LinkedList<BigDecimal> stack) {
		prevStack.push(new LinkedList<>());
		return stack = new LinkedList<>();
	}
	
	public String display() {
		LinkedList<BigDecimal> stack;
		try {
			stack = calculate(input);
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

