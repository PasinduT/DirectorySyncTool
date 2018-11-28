import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileUploader extends Thread implements Runnable{
	private String header;
	private File file;
	private Socket socket;
	private DataOutputStream output;
	
	public FileUploader(String header, String filename, String host, int port) throws UnknownHostException, IOException
	{
		this.header = header;
		this.file = new File(filename);
		this.socket = new Socket(host, port);
		this.output = new DataOutputStream(socket.getOutputStream());
	}
	
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
