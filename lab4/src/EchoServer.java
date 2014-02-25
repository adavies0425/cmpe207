

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 4
 * 
 * EchoServer: Receives messages from the EchoClient.
 */

public class EchoServer
{
	private static final int SERVER_PORT = 8888;
	private static final String QUIT_COMMAND = "QUIT";
	
	private ServerSocket socket = null;
	
	private void initialize() throws IOException
	{
		socket = new ServerSocket(SERVER_PORT);
	}
	
	private void cleanup() throws IOException
	{
		if(socket != null)	socket.close();
	}
	
	private Socket waitForConnection() throws IOException
	{
		if(socket == null)
		{
			throw new IOException("Server socket not initialized!");
		}
		
		return socket.accept();
	}
	
	private boolean processClientRequest(Socket clientSocket) throws IOException
	{
		BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		PrintWriter outStream = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream(), 1024), false);
		
		String message = inStream.readLine();
		System.out.println("Server -- Got message: " + message);
		if(message.equalsIgnoreCase(QUIT_COMMAND))
		{
			return true;
		}
		
		outStream.write(message + "\n");
		outStream.flush();
		return false;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		EchoServer server = null;
		
		try
		{
			server = new EchoServer();
			server.initialize();
			System.out.println("Server up and running on port " + SERVER_PORT + "...");
			
			boolean quit = false;
			Socket clientSocket = server.waitForConnection();
			while(!quit)
			{
				quit = server.processClientRequest(clientSocket);
			}
			clientSocket.close();
			
			System.out.println("Shutting down server.");
			server.cleanup();
		}
		catch(Exception e)
		{
			System.out.println("Error - an exception occurred!");
			e.printStackTrace();
		}
	}

}
