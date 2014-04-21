/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 6
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
	public static final int THREAD_PRIORITY_LOW = 0;
	public static final int THREAD_PRIORITY_MEDIUM = 1;
	public static final int THREAD_PRIORITY_HIGH = 2;
	
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
		int priority = Thread.NORM_PRIORITY;
		if(args != null && args.length > 0)
		{	
			// check if priority is specified
			if(args.length > 1)
			{
				try
				{
					int userPriority = Integer.parseInt(args[1]);
					switch(userPriority)
					{
						case THREAD_PRIORITY_LOW:
							System.out.println("User specified MIN thread priority!");
							priority = Thread.MIN_PRIORITY;
							break;
							
						case THREAD_PRIORITY_HIGH:
							System.out.println("User specified MAX thread priority!");
							priority = Thread.MAX_PRIORITY;
							break;
							
						default:
							System.out.println("User specified NORMAL thread priority!");
							priority = Thread.NORM_PRIORITY;
							break;
					}
				}
				catch(Exception e)
				{
					System.out.println("Exception while parsing priority as integer.");
					e.printStackTrace();
				}
			}
		}
		
		/* Start server and wait for connections. */
		
		try
		{
			server = EchoServer.getInstance();
			server.initialize();
			System.out.println("Server up and running on port " + SERVER_PORT + "...");
			
			while(true)
			{
				Socket clientSocket = server.waitForConnection();
				Thread clientThread;
				clientThread = new EchoServerThread(clientSocket);
				clientThread.setPriority(priority);
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
