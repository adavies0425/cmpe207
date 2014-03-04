/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 5
 * 
 * EchoServerRunnable: Implements support for multiple clients by implementing
 * the Runnable interface.
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoServerRunnable implements Runnable
{
	private Socket clientSocket;
	private BufferedReader inStream;
	private PrintWriter outStream;
	
	public EchoServerRunnable(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}
	
	public void run()
	{
		System.out.println("Client " + this.clientSocket.getInetAddress() + " connected.");
		
		boolean keepRunning = true;
		while(keepRunning)
		{
			try
			{
				this.inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				this.outStream = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream(), 1024), false);
				
				String message = inStream.readLine();
				System.out.println("Client " + clientSocket.getInetAddress() + " -- Got message: " + message);
				if(message.equalsIgnoreCase(EchoServer.QUIT_COMMAND))
				{
					keepRunning = false;
					break;
				}
				
				outStream.write(message + "\n");
				outStream.flush();
			}
			catch(Exception e)
			{
				System.out.println("Error - an exception occurred!");
				e.printStackTrace();
			}
		}
		
		System.out.println("Client " + this.clientSocket.getInetAddress() + " disconnected.");
		
		// clean up
		try
		{
			this.clientSocket.close();
			this.outStream.close();
			this.inStream.close();
		}
		catch(Exception e)
		{
			System.out.println("Error - an exception occurred!");
			e.printStackTrace();
		}
	}
}
