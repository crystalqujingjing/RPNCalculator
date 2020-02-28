import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

public class RPNDecimalCalculator implements RPNCalculator<BigDecimal>{
	LinkedList<BigDecimal> stack;
	LinkedList<LinkedList<BigDecimal>> prevStack;// keep track of each operation's result
	Map<String, Operator<BigDecimal>> opMap;// map string to operator
	Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$"); // pattern for a real number
	
	public RPNDecimalCalculator() {
		stack = new LinkedList<>();
		prevStack = new LinkedList<>();
		opMap = new HashMap<>();
		loadOperators();
	}
	
	private void loadOperators() {
		opMap.put("+", new AddDecimal());
		opMap.put("-", new SubtractDecimal());
		opMap.put("*", new MultiplyDecimal());
		opMap.put("/", new DivideDecimal());
		opMap.put("sqrt", new SqrtDecimal());
	}
	
	@Override
	public LinkedList<BigDecimal> calculate(String input) throws ValidationException {
		String s = input + " ";// Add a whitespace to the string for string manipulation
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
					if (opMap.containsKey(str)) {
						opMap.get(str).process(stack, str, prev + 1);
						prevStack.push(deepCopy(stack));
					} else {
						switch (str) {
						case "undo": undo(); break;
						case "clear": clear(); break;
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
		for (BigDecimal b : stack) {
			copy.add(b);
		}
		return copy;
	}
	
	private void undo() {
		if (prevStack.isEmpty()) return;
		prevStack.pop();
		if (prevStack.isEmpty()) stack = new LinkedList<BigDecimal>();
		else stack = prevStack.peek();
	}
	
	private void clear() {
		prevStack.push(new LinkedList<>());
		stack = new LinkedList<>();
	}
	
	public String display(LinkedList<BigDecimal> stack) {
		String res = new String();
		for (int i = stack.size() - 1; i >= 0; i--) {
			res = String.join(" ", res, stack.get(i).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
		}
		// Remove trailing empty space
		return res.trim();
	}
	
}

