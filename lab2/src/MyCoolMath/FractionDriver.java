package MyCoolMath;

/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 2
 * 
 * FractionDriver: Tests the Fraction class.
 */

import java.util.Scanner;

public class FractionDriver
{
	private static final String ADD_OPTION = "A";
	private static final String SUB_OPTION = "S";
	private static final String QUIT_OPTION = "Q";
	
	/**
	 * Runs the tests; entry point of application.
	 * @param args Not used.
	 */
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		String selection = null;
		do
		{
			printMenu();
			System.out.print("Enter option: ");
			// the nextLine() eats LF and CR
			selection = scanner.next(); scanner.nextLine();
			
			if(selection.toUpperCase().equals(ADD_OPTION))
			{
				performAddition();
			}
			else if(selection.toUpperCase().equals(SUB_OPTION))
			{
				performSubtraction();
			}
		}
		while(selection != null && !selection.toUpperCase().equals(QUIT_OPTION));
	}
	
	private static void printMenu()
	{
		System.out.println("Menu:");
		System.out.println(" - A: Add two fractions.");
		System.out.println(" - S: Substract two fractions.");
		System.out.println(" - Q: Quit");
	}
	
	private static Fraction readFractionFromInput(String which)
	{
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter Numerator for " + which + ": ");
		int n = scanner.nextInt(); scanner.nextLine();
		System.out.print("Enter Denominator for " + which + ": ");
		int d = scanner.nextInt(); scanner.nextLine();
		return new Fraction(n, d);
	}
	
	private static void performAddition()
	{	
		Fraction fractionA = readFractionFromInput("A");
		Fraction fractionB = readFractionFromInput("B");
		Fraction result = Fraction.add(fractionA, fractionB);
		System.out.println(fractionA + " + " + fractionB + " = " + result + "\n");
	}
	
	private static void performSubtraction()
	{
		Fraction fractionA = readFractionFromInput("A");
		Fraction fractionB = readFractionFromInput("B");
		Fraction result = Fraction.subtract(fractionA, fractionB);
		System.out.println(fractionA + " - " + fractionB + " = " + result + "\n");
	}
}
