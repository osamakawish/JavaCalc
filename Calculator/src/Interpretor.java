import java.util.HashSet;
import java.util.regex.Pattern;
import java.lang.reflect.*;

public class Interpretor {
	static String QUIT = "QUIT"; static int PRECEDENCES = 2;
	static char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	public Interpretor() throws NoSuchMethodException, SecurityException {
		Operators OPERATORS = new Operators();
	}

	static public String interpret(String input) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if (!checkSyntax(input)) {
			return "Syntax Error.";
		}
		
		// Compute the math.
		return compute(input);
		
	}
	
	/**
	 * Determines if brackets are used correctly.
	 * @param s
	 * @return
	 */
	private static boolean checkBrackets(String s) {
		
		// Count the total number of left and right brackets.
		int leftBrackets = 0; int rightBrackets = 0;
		// Determine the index of first left bracket and last right bracket.
		int firstLeft = -1; int lastRight = -1;
		
		// First, check the bracket count.
		for (int i = 0; i < s.length(); i++) {
			// Count the left brackets in string s.
			if (s.charAt(i) == '(') {
				leftBrackets += 1;
				if (firstLeft == -1) {
					firstLeft = i;
				}
			}
			// Count the right brackets in string s.
			else if (s.charAt(i) == ')') {
				rightBrackets += 1; 
				lastRight = i;
			}
		}
		
		if (leftBrackets != rightBrackets) {return false;}
				
		// Now check if the brackets are paired correctly.
		// For this, it's enough to check that the first bracket is a left and last is a right.
		if (firstLeft > lastRight) {return false;}
		
		return true;
				
	}
	
	/**
	 * Returns the string without its brackets.
	 * @param s
	 * @return
	 */
	private static String removeBrackets(String s) {
		
		// Stores all the content in the string s, but without the brackets.
		String toReturn = "";
		
		// Concatenates to end of toReturn if it's not a bracket.
		for (int i=0; i < s.length(); i++) {
			String ch = s.substring(i,i+1);
			if (ch != "(" || ch != ")") {toReturn = toReturn.concat(ch);}
		}
		
		return toReturn;
	}
	
	/**
	 * Returns true iff the syntax is appropriate.
	 * @param input
	 * @return
	 */
	private static boolean checkSyntax(String input) {
		
		// String input without its brackets.
		String noBrackets = removeBrackets(input);
		
		// Checks the syntax after brackets removed from input.
		// \\s*: To ignore white spaces. \\d+: To reach first number. \\s*: Ignore white spaces again: numbers
		String WHITES = "\\s*"; String NUMBER = "\\d+"; 
		
		String ADDITION = "\\+"; String MULTIPLICATION = "\\*"; String SUBTRACTION = "-"; String DIVISION = "/";
		String FUNC = "[" + ADDITION + MULTIPLICATION + SUBTRACTION + DIVISION + "]";
		
		String PATTERN = WHITES + NUMBER + WHITES + "(?:" + FUNC + WHITES + NUMBER + WHITES + ")*";
		
		// [numbers, operators]* Repeat number then operator cycle. 
		// (To add: ends in a number)
		boolean toReturn = Pattern.matches(PATTERN, noBrackets) || noBrackets.isEmpty();
		
		return checkBrackets(input) && toReturn;
	}
	
	/**
	 * Return true iff string s has brackets.
	 * @param s
	 * @return
	 */
	public static boolean hasBrackets(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (isBracket(s.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true iff charachter ch is a bracket.
	 * @param ch
	 * @return
	 */
	private static boolean isBracket(char ch) {
		
		return (ch == '(' || ch == ')');
	}

	/**
	 * Compute math assuming proper syntax. 
	 * @param input
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 */
	public static String compute(String input) throws NoSuchMethodException, IllegalAccessException,
	IllegalArgumentException, InvocationTargetException {
		
		// Return empty string if input is empty string.
		if (isEmpty(input)) {return "";}
		
		// Return constant if input is constant.
		else if (isConstant(input)) {return constant(input);}
		
		// Case: input has no brackets.
		else if (!hasBrackets(input)) {
			// index: Keep record of index of L, for computing the array L.
			int index = 0; 
			// L: The input string separating numbers and operators.
			String[] L = input.split("\\s");
			
			// Computing the array L.
			
			// p: Keeps record of the precedence of computation.
			// ie. if p = 1, compute only multiplication and division.
			int p = 1;
			while (p < PRECEDENCES) {
				for (String item : L) {
					
					if (precedence(p).contains(item)) {
						// result: The result of evaluating at index index.
						String[] result = new String[1];
						result[0] = evaluateAt(L, index); // Evaluates L at operator with index.
						
						// Place the result at index index, replacing [x, +*-/, y] with result [r]
						// ie. Replace the 3 inputs used for evaluation with the one result in array L.
						L = place(index-1, index+1, result, L);
					}
			
				index++; // Update index of computation over L.
				}
			
			// Update precedence and restart from start of the start of L.
			p++; index = 0;
			}
			
			// By this point, we've covered every precedence.
			// Return the result.
			if (!(L.length == 1)) {
				// For testing purposes only.
				System.out.println(print(L));
				System.out.println("Length of L too long: " + L.length);
				} 
			else {return L[0];}
		}
		
		// From here on, we have the case of where the computation contains brackets.
		String[] L = input.split(" ");
		
		// Figure out the indices of the first pair of brackets not containing any other brackets.
		int[] next = nextGroup(L);
		// Then compute the next group.
		String[] L0 = subArray(L, next[0], next[1]); 
		L0 = compute(combine(L0, " ")).split(" ");
		
		// Now place L0 inside L, but without the brackets.
		L = place(next[0], next[1], L0, L);
		
		input = combine(L, " ");
		return compute(input); // Compute the remainder of the input.
	}
	
	/**
	 * Place l0 in string l between indices i and j, such that strings at indices i and j are not included.
	 * @param i
	 * @param j
	 * @param l0
	 * @param l
	 * @return
	 */
	private static String[] place(int i, int j, String[] l0, String[] l) {
		
		String[] toReturn = new String[i + l0.length + l.length-j];
		
		// Equate indices up to i, not including i'th index.
		for (int index=0; index < i; index++) {
			toReturn[index] = l[index];
		}
		
		int index = i+1; // To access elements from l0.
		// Then add everything from l0.
		for (int index0=0; index0 < l0.length; index0++) {
			toReturn[index] = l0[index0]; index++;
		}
		
		// Finally, add everything beyond the j'th index.
		for (int indexL=j+1; indexL<l.length; indexL++) {
			toReturn[index] = l[indexL]; index++;
		}
		
		// And then return the array.
		return toReturn;
	}
	
	/**
	 * Take the subarray of l between indices i and j, not including characters at either end.
	 * @param l
	 * @param i
	 * @param j
	 * @return
	 */
	private static String[] subArray(String[] l, int i, int j) {
		
		String[] toReturn = new String[j-i-1]; 
		int indexL = i+1; // Index of array l. To access array l and add its elements to toReturn
		
		for (int indexR = 0; indexR < toReturn.length; indexR++) {
			toReturn[indexR] = l[indexL]; indexL++; // Adds indexL'th element to array toReturn.
		}
		
		// Returns the subarray.
		return toReturn;
	}
	
	/**
	 * Find the next group of characters in l between a pair of brackets that have no brackets in between them.
	 * Returns index of left and right brackets.
	 * @param l
	 * @return
	 */
	private static int[] nextGroup(String[] l) {
		
		int[] toReturn = new int[2];
		// Keep going until right brackets.
		int right = 1;
		while (right < l.length) {
			if (l[right] != ")") {right++;} 
		}
		
		// Go back until left bracket.
		int left = right-1;
		while (left > 0) {
			if (l[left] != "(") {left--;}
		}
		
		toReturn[0] = left; toReturn[1] = right;
		
		return toReturn;
	}
	
	/**
	 * Combine the strings in l together, connected by string string.
	 * @param l
	 * @param string
	 * @return
	 */
	private static String combine(String[] l, String string) {
		
		String toReturn = ""; int index=0;
		
		while (index < l.length-1) {
			toReturn += l[index] + string;
			index++;
		}
		toReturn += l[l.length-1];
		
		return toReturn;
	}
	
	/**
	 * Evaluate the computation of the operator at index index.
	 * @param l
	 * @param index
	 * @return 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private static String evaluateAt(String[] l, int index) throws NoSuchMethodException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		String num1 = l[index-1]; String num2 = l[index+1];
		int[] args = {Integer.parseInt(num1), Integer.parseInt(num2)};
		String func = l[index]; Method Func = Operators.getMethod(func);
		
		int result = (int) Func.invoke(null, args);
		
		return Integer.toString(result);
	}
	
	/**
	 * Returns a set of operators with precedence i.
	 * @param i
	 * @return
	 */
	private static HashSet<String> precedence(int i) {
		
		// Creates a hashSet for returning.
		HashSet<String> toReturn = new HashSet<>();
		
		switch (i) {
		case 1:
			// Multiplication and division have precedence 1.
			toReturn.add("*"); toReturn.add("/");
			break;
		case 2:
			// Addition and Subtraction have precedence 2.
			toReturn.add("+"); toReturn.add("-");
		default:
			break;
		}
		return toReturn;
	}
	
	/**
	 * Returns the input as a constant without any spaces.
	 * @param input
	 * @return
	 */
	private static String constant(String input) {
		
		return input.split(" ")[0];
	}
	
	/**
	 * Returns true iff input is a number.
	 * @param input
	 * @return
	 */
	private static boolean isConstant(String input) {
		
		String[] L = input.split(" "); // Store list without spaces.
		if (L.length != 1) {return false;} // Assume list has exactly 1 element.
		String N = L[0]; // Refer to element as N.
		
		// Make sure every character in list is a digit.
		int index = 0;
		while (index < N.length()) {
			if (!isDigit(N.charAt(index))) {
				return false;
			}
			index++;
		}
		return true;
	}
	
	/**
	 * Returns true iff ch is a digit.
	 * @param ch
	 * @return
	 */
	private static boolean isDigit(char ch) {
		
		for (char c: DIGITS) { // DIGITS is an array (stored at start of class) containing all digit characters.
			if (c == ch) {return true;}
		}
		return false;
	}

	/**
	 * Returns true iff input contains no empty spaces.
	 * @param input
	 * @return
	 */
	private static boolean isEmpty(String input) {
		
		return input.split(" ").length == 0;
	}
	
	public static String print(String[] S) {
		String toReturn = "{";
		
		for (String item: S) {toReturn += item;}
		toReturn += "}";
		
		return toReturn;
	}
}
