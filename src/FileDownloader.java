import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class implements a Thread which will download a file from a desired stream, 
 * to a user-defined location on the computer.
 * @author Pasindu Tennakoon
 *
 */
public class FileDownloader extends Thread implements Runnable{
	private FileOutputStream output;
	private DataInputStream input;
	
	/**
	 * Constructor
	 * @param filename The filename and path of where the file should be downloaded to
	 * @param input The input stream which will bring the file data 
	 * @throws IOException When the file could not be opened for writing
	 */
	public FileDownloader(String filename, DataInputStream input) throws IOException
	{
		this.output = new FileOutputStream(filename);
		this.input = input;
	}
	
	/**
	 * Download a file that is sent
	 * @see java.lang.Thread#run()
	 */
	public void run() 
	{
		try {
			// Read the file length (not really used, but for future expansions might be useful)
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
