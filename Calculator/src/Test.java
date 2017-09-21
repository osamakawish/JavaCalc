import java.lang.reflect.InvocationTargetException;

public class Test {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Interpretor I = new Interpretor();
		
		System.out.println(Interpretor.print( new String[] {"1","+","1"} ));
		
		String toTest = "1 + 1";
		System.out.println(I.print(toTest.split(" ")));
		System.out.println("Testing: " + toTest);
		System.out.println(I.interpret(toTest));

	}

}
