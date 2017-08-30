import java.util.HashSet;
import java.util.regex.Pattern;

public class Interpretor {
	static String QUIT = "QUIT";
	static HashSet<String> BRACKETS = ['(', ')'];

	static public String interpret(String input) throws Exception {
		
		if (!checkSyntax(input)) {
			throw new Exception("Syntax Error.");
		}
		
		// Compute the math.
		String result = compute(input);
		
		return ""; // Only returning empty string to hide error.
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
		
		// Stores all the content in the string, but without the bracket.
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
		// [\\+ (addition) \\* (multiplication) - (subtraction) ^ (exponentiation) / (division) ]: operators
		// [numbers, operators]* Repeat number then operator cycle. 
		// (To add: ends in a number)
		boolean toReturn = Pattern.matches("[\\s*\\d+\\s*[\\+\\*-^/]\\s*]*", noBrackets);
		
		return checkBrackets(input) && toReturn;
	}
	
	public static boolean noBrackets(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i))
		}
	}
	
	/**
	 * Compute math assuming proper syntax. 
	 * @param input
	 * @return
	 */
	private static String compute(String input) {
		
		if (noBrackets(input)) {
			
		}
		
		return "";
	}
}
