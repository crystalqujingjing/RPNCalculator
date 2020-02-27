import java.util.LinkedList;

public abstract class Operator<T> {
	public abstract void process(LinkedList<T> stack, String str, int pos) throws ValidationException;
}
