package MyCoolMath;

/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 2
 * 
 * Fraction: Represents a fractional number.
 */

/**
 * Fraction: Represents a fractional number.
 * @author mujtaba.hassanpur
 */
public class Fraction
{
	private int numerator;
	private int denominator;
	
	/**
	 * Default constructor. Creates number zero (0/1).
	 */
	public Fraction()
	{
		this(0, 1); // call other constructor
	}
	
	/**
	 * Constructs an instance of a Fraction class.
	 * @param n Value of the numerator.
	 * @param d Value of the denominator.
	 */
	public Fraction(int n, int d)
	{
		if(d < 0)
		{
			d *= -1;
			n *= -1;
		}
		
		if(d != 0)
		{
			this.numerator = n;
			this.denominator = d;
		}
		else // invalid parameters
		{
			this.numerator = 0;
			this.denominator = 1;
		}
		
		reduce();
	}
	
	/**
	 * Returns the numerator.
	 * @return Returns the numerator.
	 */
	public int getNumerator()
	{
		return this.numerator;
	}
	
	/**
	 * Returns the denominator.
	 * @return Returns the denominator.
	 */
	public int getDenominator()
	{
		return this.denominator;
	}
	
	/**
	 * Sets the numerator.
	 * @param n Value of the numerator.
	 */
	public void setNumerator(int n)
	{
		this.numerator = n;
	}
	
	/**
	 * Sets the denominator.
	 * @param d Value of the denominator. Cannot be zero.
	 */
	public void setDenominator(int d)
	{
		if(d != 0)
		{
			this.denominator = d;
		}
	}
	
	/**
	 * Returns the fraction value as a float.
	 * @return Returns the fraction value as a float.
	 */
	public float getFractionValueAsFloat()
	{
		return (float)this.numerator / (float)this.denominator;
	}
	
	/**
	 * Adds two fractions.
	 * @param a Reference to Fraction A.
	 * @param b Reference to Fraction B.
	 * @return Fraction with result of A + B.
	 */
	public static Fraction add(Fraction a, Fraction b)
	{
		Fraction result = null;
		if(a != null && b != null)
		{
			int na = a.numerator * b.denominator;
			int nb = b.numerator * a.denominator;
			int den = a.denominator * b.denominator;
			int num = na + nb;
			result = new Fraction(num, den);
		}
		
		return result;
	}
	
	/**
	 * Subtracts two fractions.
	 * @param a Reference to Fraction A.
	 * @param b Reference to Fraction B.
	 * @return Fraction with result of A - B.
	 */
	public static Fraction subtract(Fraction a, Fraction b)
	{
		Fraction result = null;
		if(a != null && b != null)
		{
			int na = a.numerator * b.denominator;
			int nb = b.numerator * a.denominator;
			int den = a.denominator * b.denominator;
			int num = na - nb;
			result = new Fraction(num, den);
		}
		
		return result;
	}
	
	private void reduce()
	{
		while(this.numerator % 2 == 0 && this.denominator % 2 == 0)
		{
			this.numerator /= 2;
			this.denominator /= 2;
		}
	}
	
	/**
	 * Returns the string representation of the Fraction object.
	 */
	public String toString()
	{
		if(Math.abs(this.numerator) == Math.abs(this.denominator))
		{
			if(this.numerator/this.denominator > 0)
			{
				return "1";
			}
			else
			{
				return "-1";
			}
		}
		else
		{
			String sign = "";
			if(this.numerator < 0)
			{
				sign = "-";
			}
			return sign + Math.abs(this.numerator) + "/" + Math.abs(this.denominator);
		}
	}
}
