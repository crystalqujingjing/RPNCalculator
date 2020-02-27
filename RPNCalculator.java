import java.util.LinkedList;

public interface RPNCalculator<T> {
	public LinkedList<T> calculate(String input) throws Exception;
	
	public String display();
}
