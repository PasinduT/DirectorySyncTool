import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class is the server side of the File Sync module
 * It will accept listen for, and accept connections, receive
 * and redirect commands
 * @author Pasindu Tennakoon
 *
 */
public class Server 
{
	private String homeDirectory;
	private int port;
	private String filename;
	
	/**
	 * Constructor
	 * @param filename the path of the file that the info file from the client will be stored into
	 * @param homeDirectory the path of the directory where the backup will be stored into
	 * @param port the port number which will be listening to incoming connections
	 */
	public Server(String filename, String homeDirectory, int port) 
	{
		this.filename = filename;
		this.homeDirectory = homeDirectory;
		this.port = port;
	}
	
	/**
	 * Listens and accepts requests from the
	 * @throws IOException
	 */
	public void listen() throws IOException 
	{
		ServerSocket server = new ServerSocket(port);
		
		while(true)
		{
			Socket socket = server.accept();
			DataInputStream input = new DataInputStream(socket.getInputStream());
			String command = input.readUTF();
			
			if (command.equals("DIRECINFO"))
			{
				FileDownloader downloader = new FileDownloader(filename, input);
				downloader.start();
			}
			
			else if (command.equals("MAIN")) 
			{
				FileComparer fileComparer = new FileComparer(homeDirectory, socket, filename);
				fileComparer.start();
			}
			
			else if (command.startsWith("FILE"))
			{
				String pathName = command.substring(5);
				System.out.println("Recieving file:" + pathName);
				FileDownloader downloader = new FileDownloader(homeDirectory + pathName, input);
				downloader.start();
			}
		}
	}
	
	public static void main(String [] a) {
		Server s = new Server("/Users/pasindutennakoon/Desktop/testing.ser", "/Users/pasindutennakoon/Desktop/testing", 4999);
		try {
			s.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
