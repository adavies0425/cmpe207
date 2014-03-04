/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 5
 * 
 * EchoServerThread: Implements support for multiple clients by extending the
 * Thread class.
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoServerThread extends Thread
{
	private Socket clientSocket;
	private BufferedReader inStream;
	private PrintWriter outStream;
	
	public EchoServerThread(Socket clientSocket)
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
