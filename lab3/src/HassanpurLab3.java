import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 3
 * February 17, 2014
 * 
 * HassanpurLab3 - Connects to www.yahoo.com and prints out the URL information
 * as well as the contents of the web page to the console.
 */

public class HassanpurLab3
{
	public static void main(String[] args) 
	{
		try
		{
			URL url = new URL("http://www.yahoo.com/");
			System.out.println("Protocol: " + url.getProtocol());
			System.out.println("Host: " + url.getHost());
			System.out.println("Filename: " + url.getFile());
			System.out.println("Port: " + url.getPort());
			System.out.println("Reference: " + url.getRef() + "\n");
			
			System.out.println("Content of page:\n");
			BufferedReader buffer = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while((line = buffer.readLine()) != null)
			{
				System.out.println(line);
			}
			buffer.close();
		}
		catch(MalformedURLException e)
		{
			System.out.println("Error - MalformedURLException: " + e);
		}
		catch(IOException e)
		{
			System.out.println("Error - IOException: " + e);
		}
	}
}
