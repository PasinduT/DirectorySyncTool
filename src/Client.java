import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;

public class Client {
	private String homeDirectory;
	private String filename;
	private String host;
	private int port;
	
	/**
	 * Constructor
	 * @param filename the filename and path of the file where directory information will be stored to
	 * @param homeDirectory the path name of the directory which will be scanned
	 * @param host the string representation of the address of the server. Can be an ip address or a dns name
	 * @param port the port number the server is listening on
	 */
	public Client(String filename, String homeDirectory, String host, int port)
	{
		this.filename = filename;
		this.homeDirectory = homeDirectory;
		this.host = host;
		this.port = port;
	}

	/**
	 * Scans the directory, makes an info file and sends it to the server. Finally, it also
	 * listens for server feedback and responds appropriately.
	 * @throws IOException If errors are made during the stage when the socket is trying to connect
	 * 	or the streams are trying to read and write data.
	 * @throws ClassNotFoundException when dependency class CustomFile could not be found, this error will be 
	 * raised
	 */
	public void scanAndUpdate() throws IOException, ClassNotFoundException
	{
		// Scan the directory, make the info file and send it to the server
		DirectoryInfoFile directoryInfoFile = new DirectoryInfoFile();
		directoryInfoFile.makeDirectoryInfoFile(filename, Paths.get(homeDirectory));
		directoryInfoFile.sendDirectoryFile(host, port, filename);
		
		// Connect to the server to listen to feedback
		Socket socket = new Socket(host, port);
		DataInputStream input = new DataInputStream(socket.getInputStream());
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		
		// Notify the server that this is the main listening thread
		output.writeUTF("MAIN");
		
		// Get command from the server
		String command = input.readUTF();
		// As long as server does not send the exit message, keep listening
		while(!command.equals("QUIT")) {
			
			// If the server requests for a file, get the file object the server is sending
			// and make a new thread which uploads the requested file to the server
			if (command.equals("FILE")) {
				CustomFile file;
				ObjectInputStream in = new ObjectInputStream(input);
				file = (CustomFile) in.readObject();
				FileUploader fileUploader = new FileUploader("FILE " + file.getRelativePath(), file.getPath().toString(), host, port);
				fileUploader.start();
			}
			
			// Read the next command
			command = input.readUTF();
		}
		
		// Close all streams and the socket
		input.close();
		output.close();
		socket.close();
	}
	
	public static void main(String [] a) {
		Client c =  new Client("direcInfo.ser", "/Users/pasindutennakoon/Desktop/CS250", "localhost", 4999);
		try {
			c.scanAndUpdate();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
