/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 6
 * 
 * EchoServerThread: Implements support for multiple clients by extending the
 * Thread class.
 */

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoServerThread extends Thread
{
	private Socket clientSocket;
	private DataInputStream inStream;
	private PrintWriter outStream;
	
	private static final String SERVER_OK_RESPONSE = "OK\n";
	
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
				this.inStream = new DataInputStream(clientSocket.getInputStream());
				this.outStream = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream(), 1024), false);
				
				// read file name
				int fnamesz = inStream.readInt();
				System.out.println("Client " + clientSocket.getInetAddress() + " -- Filename length: " + fnamesz);
				byte[] bfname = new byte[fnamesz];
				for(int i = 0; i < fnamesz; i++)
				{
					inStream.read(bfname, i, 1);
				}
				String filename = new String(bfname);
				System.out.println("Client " + clientSocket.getInetAddress() + " -- Receiving file: " + filename);
				
				// read file contents and write to disk
				File file = new File("server/" + filename);
				FileOutputStream fileStream = new FileOutputStream(file);
				long fsize = inStream.readLong();
				byte[] buffer = new byte[64 * 1024];
				int count;
				long rembytes = fsize;
				while((count = inStream.read(buffer)) != -1)
				{
					fileStream.write(buffer, 0, count);
					rembytes -= count;
					if(rembytes <= 0)
					{
						// done
						break;
					}
				}
				System.out.println("Client " + clientSocket.getInetAddress() + " -- Received file: " + filename + " " + fsize + " bytes.");
				fileStream.close(); // cleanup
				
				// send OK response
				outStream.write(SERVER_OK_RESPONSE);
				outStream.flush();
				
				// we're done
				keepRunning = false;
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
