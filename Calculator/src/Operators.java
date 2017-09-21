import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Operators {
	static Map<String, Method> METHODS = new HashMap<String, Method>();
	String ADD = "+"; String MULTIPLY = "*"; String SUBTRACT = "-"; String DIVIDE = "/";
	private static Class[] inputTypes = {float.class , float.class}; 
	
	Operators() throws NoSuchMethodException, SecurityException {
		METHODS.put(ADD, getMethod("add"));
		METHODS.put(MULTIPLY, getMethod("multiply"));
		METHODS.put(SUBTRACT, getMethod("subtract"));
		METHODS.put(DIVIDE, getMethod("divide"));
	}
	
	static Method getMethod(String s) throws NoSuchMethodException {
		return Operators.class.getMethod(s, inputTypes);
	}
	
	public static float add(float x, float y) {
		return x+y;
	}
	
	public static float multiply(float x, float y) {
		return x*y;
	}
	
	public static float subtract(float x, float y) {
		return x-y;
	}
	
	public static float divide(float x, float y) {
		return x/y;
	}
}
