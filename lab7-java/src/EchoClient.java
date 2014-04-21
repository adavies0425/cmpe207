

/**
 * Mujtaba Hassanpur
 * San Jose State University
 * CMPE 207 - Lab 6
 * 
 * EchoClient: Sends a message to the EchoServer.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class EchoClient
{
	private static final String SERVER_URL = "http://www.neatwurk.com/lab7/index.php";
	
	/**
	 * Sends the contents of the given filename to the server.
	 * @param filename Filename of the file to be sent.
	 * @returns HttpUrlConnection response code.
	 * @throws IOException
	 */
	private int sendFileToServer(String filename) throws IOException
	{
		int result = -1;
		File file = new File(filename);
		System.out.println("File size for " + file.getName() + " is " + file.length() + " bytes.");
		HttpURLConnection httpPost = (HttpURLConnection) new URL(SERVER_URL).openConnection();
		if(httpPost != null)
		{
			httpPost.setDoInput(true);
			httpPost.setDoOutput(true);
			httpPost.setAllowUserInteraction(false);
			httpPost.setRequestMethod("POST");
			String boundary = "----FormBoundary" + UUID.randomUUID().toString().replaceAll("-", "");
			String NEWLINE = "\r\n";
			httpPost.setRequestProperty("Connection", "keep-alive");
			//httpPost.setRequestProperty("enctype", "multipart/form-data");
			httpPost.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			httpPost.setRequestProperty("User-Agent", "Mujtaba EchoClient 1.0");
			
			// build the string going before binary data
			StringBuilder req = new StringBuilder();
			req.append("--" + boundary).append(NEWLINE);
			req.append("Content-Disposition: form-data; name=\"upload\"; filename=\"" + file.getName() + "\"").append(NEWLINE);
			String contentType = URLConnection.guessContentTypeFromName(filename);
			if(contentType == null) contentType = "application/octet-stream"; // in case we can't tell what it is
			req.append("Content-Type: " + contentType).append(NEWLINE).append(NEWLINE);
			//req.append("Content-Transfer-Encoding: binary").append(NEWLINE).append(NEWLINE);
			
			// build the string going after binary data
			StringBuilder req2 = new StringBuilder();
			req2.append(NEWLINE).append("--" + boundary).append(NEWLINE);
			req2.append("Content-Disposition: form-data; name=\"submit\"").append(NEWLINE).append(NEWLINE);
			req2.append("submit").append(NEWLINE);
			req2.append(boundary + "--").append(NEWLINE);
			
			// Calculate content length
			long length = req.length() + file.length() + req2.length();
			//httpPost.setRequestProperty("Content-Length", "" + length);
			
			System.out.println("Length: req=" + req.length() + ", req2=" + req2.length() + ", file=" + file.length());
			System.out.println("** Content-Length: " + length);
			System.out.println("Sending to server:\n");
			System.out.println(req + "--binary-data--\n" + req2);
			
			// open the connection
			OutputStream outStream = httpPost.getOutputStream();
			
			// Add the file upload field and do the upload
			outStream.write(req.toString().getBytes());
			//PrintWriter writer = new PrintWriter(new OutputStreamWriter(outStream, "UTF8"), true);
			//writer.append(req);
			//writer.flush();
			
			// Read and upload the file now
			FileInputStream inStream = new FileInputStream(file);
			byte[] buffer = new byte[4*1024];
			int count = -1;
			while((count = inStream.read(buffer)) != -1)
			{
				outStream.write(buffer, 0, count);
			}
			//outStream.flush();
			
			// more stuff after the file
			outStream.write(req2.toString().getBytes());
			
			// Get response from server
			StringBuilder stringBuilder = new StringBuilder();
			try
	        {
	            InputStream stream = httpPost.getInputStream();
	            int b;
	            while ((b = stream.read()) != -1)
	            {
	                stringBuilder.append((char) b);
	            }
	            
	            System.out.println("Result from server:\n\n" + stringBuilder + "\n");
	            result = httpPost.getResponseCode();
	        }
	        catch (Exception e)
	        {
	        	e.printStackTrace();
	        }
			
			// Finish the upload and close connection
			outStream.flush();
			inStream.close();
			
			// Disconnect
			httpPost.disconnect();
		}
		
		return result;
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
				System.out.println("Connected to " + SERVER_URL + ". Sending file: " + filename + "...");
				int response = client.sendFileToServer(filename);;
				if(response == HttpURLConnection.HTTP_OK)
				{
					System.out.println("Successfully sent file: " + filename + ".");
				}
				else
				{
					System.out.println("Failed to send file: " + filename + ".");
				}
				System.out.println("Closing connection.");
			}
		}
		catch(Exception e)
		{
			System.out.println("Error - An exception occurred!");
			e.printStackTrace();
		}
	}
}
