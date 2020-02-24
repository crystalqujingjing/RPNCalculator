import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class RPNCalculator {
	LinkedList<BigDecimal> stack;
	LinkedList<LinkedList<BigDecimal>> prevStack;// keep track of each operation's result
	Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$"); // pattern for a real number
	String output = "";
	
	public RPNCalculator(String s) {
		stack = new LinkedList<>();
		prevStack = new LinkedList<>();
		String sWithPadding = s + " ";
		calculate(sWithPadding);
	}
	
	private void calculate(String s) {
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
					case "-": 
					case "*":
					case "/": 
						BigDecimal[] twoNums = new BigDecimal[2];
						for (int i = 0; i < 2; i++) {
							if (stack.isEmpty()) {
								int pos = prev + 1;
								output = "operator " + str + " (position: " + pos +"): insufficient parameters";
								return;
							}
							else {
								twoNums[i] = stack.pop();
							}
						}
						switch (str) {
						case "+": 
							stack.push(Operator.add(twoNums[0], twoNums[1]));
							break;
						case "-": 
							stack.push(Operator.subtract(twoNums[1], twoNums[0]));
							break;
						case "*": 
							stack.push(Operator.multiply(twoNums[0], twoNums[1]));
							break;
						case "/": 
							stack.push(Operator.divide(twoNums[1], twoNums[0]));
							break;
						}
						prevStack.push(deepCopy(stack));
						break;
					case "sqrt": 
						if (stack.isEmpty()) {
							output = "operator" + str + "(position: " + prev +"): insufficient parameters";
							return;
						}
						else {
							 stack.push(Operator.sqrt(stack.pop(), 15));// 15 decimal places of precision
						}
						prevStack.push(deepCopy(stack));
						break;
					case "undo": undo(); break;
					case "clear": clear(); break;
					default:
						output = "operator " + str + " is not a valid operator";
					}
				}
				prev = pointer + 1;
			}
			pointer++;
		}		
	}
	
	private LinkedList<BigDecimal> deepCopy(LinkedList<BigDecimal> stack) {
		LinkedList<BigDecimal> copy = new LinkedList<>();
		for (int i = stack.size() - 1; i >= 0; i--) {
			copy.push(stack.get(i));
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
		if (output.length() > 0) return output;
		for (int i = stack.size() - 1; i >= 0; i--) {
			String roundStr = stack.get(i).setScale(10, RoundingMode.HALF_UP).toString();
			// Remove trailing zeros
			String res = roundStr;
			for (int j = roundStr.length() - 1; j >= 0; j--) {
				if (roundStr.charAt(j) != '0') {
					if (roundStr.charAt(j) == '.') {
						res = roundStr.substring(0, j);
					}
					break;
				}
				res = roundStr.substring(0, j);
			}
			output += res + " ";
		}
		// Remove trailing empty space
		if (output.length() == 0) return "";
		return output.substring(0, output.length() - 1);
	}
	
}
