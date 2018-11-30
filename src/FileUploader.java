import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class implements a Thread which will upload a file the user specifies to
 * a socket address which the user specifies.
 * @author Pasindu Tennakoon
 *
 */
public class FileUploader extends Thread implements Runnable{
	private String header;
	private File file;
	private Socket socket;
	private DataOutputStream output;
	
	/**
	 * Constructor
	 * @param header The header information needed to be sent before uploading a file
	 * @param filename The filename and the path of the file which will be uploaded
	 * @param host The string representation of the host, could be an ip address or a DNS name
	 * @param port The port number of the server to connect to
	 * @throws UnknownHostException When there is an error trying to connect the socket
	 * @throws IOException When the file could not be opened or if there was an error while trying to connect to the server
	 */
	public FileUploader(String header, String filename, String host, int port) throws UnknownHostException, IOException
	{
		this.header = header;
		this.file = new File(filename);
		this.socket = new Socket(host, port);
		this.output = new DataOutputStream(socket.getOutputStream());
	}
	
	/**
	 * This method uploads the file
	 * @see java.lang.Thread#run()
	 */
	public void run() 
	{
		try {
			// Send directory info file information
			output.writeUTF(header);
			output.writeLong(file.length());
			
			//Create a binary file reader 
			FileInputStream fileInput = new FileInputStream(file);
			
			//Make a temporary buffer
			byte data[] = new byte[2048];
			while(true) {
				//Loop through as long as there is data
				int len = fileInput.read(data);
				if (len == -1) break;
				output.write(data, 0, len);
			}
			
			//Close input file, output stream and socket
			output.close();
			fileInput.close();
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
