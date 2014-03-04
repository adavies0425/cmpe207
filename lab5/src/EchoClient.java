

/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 4
 * 
 * EchoClient: Sends a message to the EchoServer.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EchoClient
{
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 8888;
	private static final String QUIT_COMMAND = "QUIT";
	
	private Scanner scanner;
	private Socket socket;
	private DataOutputStream outStream;
	private BufferedReader inStream;
	
	/**
	 * Initializes the socket and scanner.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void initialize() throws UnknownHostException, IOException
	{
		scanner = new Scanner(System.in);
		socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
		outStream = new DataOutputStream(socket.getOutputStream());
		inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	/**
	 * Cleans up the socket and scanner.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void cleanup() throws UnknownHostException, IOException
	{
		if(outStream != null)	outStream.close();
		if(inStream != null)	inStream.close();
		if(socket != null)		socket.close();
		if(scanner != null)		scanner.close();
	}
	
	/**
	 * Returns the user input from standard in.
	 * @return Returns the user input from Standard in.
	 * @throws IOException
	 */
	private String getUserInput() throws IOException
	{
		if(scanner == null)
		{
			throw new IOException("Scanner not initialized!");
		}
		
		return scanner.nextLine().trim();
	}
	
	/**
	 * Sends the given message to the server.
	 * @param message User message to send.
	 * @throws IOException
	 */
	private void sendToServer(String message) throws IOException
	{
		if(message == null || outStream == null)
		{
			throw new IOException("Got null object!");
		}
		
		outStream.writeBytes(message);
		outStream.writeByte('\n');
	}
	
	private String receiveFromServer() throws IOException
	{
		if(inStream == null)
		{
			throw new IOException("Got null object!");
		}
		
		String message = inStream.readLine();
		return message;
	}
	
	/**
	 * Prompts user for message. Sends the message to the EchoServer.
	 * @param args Not used.
	 */
	public static void main(String[] args)
	{
		EchoClient client = null;
		
		try
		{
			client = new EchoClient();
			client.initialize();
			System.out.println("Connected to " + SERVER_ADDRESS + ":" + SERVER_PORT + "...");
			
			String message;
			System.out.print(">> ");
			while((message = client.getUserInput()) != null)
			{	
				// write to server
				client.sendToServer(message);
				
				// get the server response
				String response = client.receiveFromServer();
				if(response != null)
				{
					System.out.println(response);
				}
				
				// check if this was a quit command
				if(message.equalsIgnoreCase(QUIT_COMMAND))
				{
					break;
				}
				
				System.out.print(">> ");
			}
			
			System.out.println("Closing connection.");
			client.cleanup();
		}
		catch(Exception e)
		{
			System.out.println("Error - An exception occurred!");
			e.printStackTrace();
		}
	}

}
