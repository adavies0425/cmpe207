/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 4
 * 
 * EchoServer: Receives messages from the EchoClient.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer
{
	public static final int SERVER_PORT = 8888;
	public static final String QUIT_COMMAND = "QUIT";
	
	private static EchoServer instance = null;
	private ServerSocket socket = null;
	
	private EchoServer()
	{
		// nothing, just want to declare it as private
	}
	
	public static synchronized EchoServer getInstance()
	{
		if(instance == null)
		{
			instance = new EchoServer();
		}
		
		return instance;
	}
	
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		EchoServer server = null;
		boolean doThread = true;
		if(args != null && args.length >= 1)
		{
			doThread = !(args[0].equalsIgnoreCase("runnable"));
		}
		
		try
		{
			server = EchoServer.getInstance();
			server.initialize();
			System.out.println("Server up and running on port " + SERVER_PORT + "...");
			
			while(true)
			{
				Socket clientSocket = server.waitForConnection();
				Thread clientThread;
				if(doThread)
				{
					clientThread = new EchoServerThread(clientSocket);
				}
				else
				{
					clientThread = new Thread(new EchoServerRunnable(clientSocket));
				}
				clientThread.start();
			}
		}
		catch(Exception e)
		{
			System.out.println("Error - an exception occurred!");
			e.printStackTrace();
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run()
			{
				System.out.println("Shutting down server.");
				try
				{
					EchoServer.getInstance().cleanup();
				}
				catch(Exception e)
				{
					System.out.println("Error - an exception occurred!");
					e.printStackTrace();
				}
			}
		});
	}

}
