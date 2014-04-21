

/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 6
 * 
 * EchoClient: Sends a message to the EchoServer.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
	private static final String SERVER_OK_RESPONSE = "OK";
	
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
	 * Sends the contents of the given filename to the server.
	 * @param filename Filename of the file to be sent.
	 * @throws IOException
	 */
	private void sendFileToServer(String filename) throws IOException
	{
		File file = new File(filename);
		int fnameLength = file.getName().length();
		long fsize = file.length();
		
		// write file name
		outStream.writeInt(fnameLength);
		byte[] fnamebytes = file.getName().getBytes();
		for(int i = 0; i < fnamebytes.length; i++)
		{
			outStream.writeByte(fnamebytes[i]);
		}
		
		// write file contents
		outStream.writeLong(fsize);
		FileInputStream finStream = new FileInputStream(file);
		byte[] buffer = new byte[64 * 1024];
		int count;
		while((count = finStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, count);
		}
		finStream.close();
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
			for(int i = 0; i < args.length; i++)
			{
				String filename = args[i];
				client = new EchoClient();
				client.initialize();
				System.out.println("Connected to " + SERVER_ADDRESS + ":" + SERVER_PORT + ". Sending file: " + filename + "...");
				client.sendFileToServer(filename);
				String response = client.receiveFromServer().trim();
				if(response.equals(SERVER_OK_RESPONSE))
				{
					System.out.println("Successfully sent file: " + filename + ".");
				}
				else
				{
					System.out.println("Failed to send file: " + filename + ".");
				}
				System.out.println("Closing connection.");
				client.cleanup();
			}
		}
		catch(Exception e)
		{
			System.out.println("Error - An exception occurred!");
			e.printStackTrace();
		}
	}
}
