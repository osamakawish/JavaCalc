import java.util.Scanner;

public class Program {

	public static void main(String[] args) throws Exception {
		
		// To interpret inputs and provide output.
		Interpretor I = new Interpretor();
		
		// To scan the inputs.
		Scanner sc = new Scanner(System.in);
		
		// Read the first line and interpret it.
		String line = sc.nextLine();
		String result = Interpretor.interpret(line);
		System.out.println(result);
		
		// Keep reading and interpreting next lines and computing them.
		while (!line.equals(Interpretor.QUIT)) {
			line = sc.nextLine();
			result = Interpretor.interpret(line);
			System.out.println(result);
		}
		
		// Close the scanner.
		sc.close();
		
	}

}
