import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDownloader extends Thread implements Runnable{
	private FileOutputStream output;
	private DataInputStream input;
	
	public FileDownloader(String filename, DataInputStream input) throws IOException
	{
		this.output = new FileOutputStream(filename);
		this.input = input;
	}
	
	public void run() 
	{
		try {
			long fileLength = input.readLong();
			//Make a temporary buffer
			byte data[] = new byte[2048];
			while(true) {
				//Loop through as long as there is data
				int len = input.read(data);
				if (len == -1) break;
				output.write(data, 0, len);
			}
			
			//Close input file, output stream and socket
			output.close();
			input.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
