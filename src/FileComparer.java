import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Paths;

/**
 * This class takes the client directory structure and the local directory structure and
 * compare it for missing and updated files. It then requests the client for these files
 * @author Pasindu Tennakoon
 *
 */
public class FileComparer extends Thread implements Runnable{
	private Directory directory;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private String homeDirectory;
	private String pathToFile;
	
	/**
	 * Constructor
	 * @param homeDirectory the directory where the backup is stored
	 * @param socket the socket that connects to the main client thread
	 * @param pathToFile the filename and path of the info file downloaded from the client
	 * @throws IOException when an error occurs while trying to connect to the client
	 */
	public FileComparer(String homeDirectory, Socket socket, String pathToFile) throws IOException 
	{
		this.pathToFile = pathToFile;
		this.socket = socket;
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
		directory = new Directory(Paths.get(homeDirectory), "/");
		directory.scan();
		this.homeDirectory = homeDirectory;
		directory.scan();
		
	}
	
	/**
	 * Compares the files in each directory, and requests for ones that need to be updated or added.
	 * Recursively, compare the files in each subdirectory as well
	 * @param foriegn the foreign directory equivalent to the current comparing one
	 * @param local the local directory currently being compared to
	 * @throws IOException when an error occurs while trying to send data to the client
	 */
	public void compareDirectories(Directory foriegn, Directory local) throws IOException 
	{
		// For every file in the foriegn directory
		for(CustomFile foriegnFile : foriegn.getFiles()) {
			boolean found = false;
			
			// Check if it has an match in the local directory
			for (CustomFile localFile : local.getFiles()) {
				// Compare the files
				int comp = foriegnFile.compareTo(localFile);
				// If the files, have the same path name and timestamps, skip it
				if (comp == 0) {
					found = true;
					break;
				}
				// Otherwise if the foriegn file is newer, then request it from the client
				// and write the CustomFile object retaining to it
				else if (comp > 0) {
					output.writeUTF("FILE");
					ObjectOutputStream out = new ObjectOutputStream(output);
					out.writeObject(foriegnFile);
					found = true;
					break;
				}
				
				// If the file is newer on the server(usually due to the fact that it file time in server is later),
				// mark it as found
				else if (foriegnFile.getRelativePath().equals(localFile.getRelativePath())) {
					found = true;
					break;
				}
			}
			
			// If the file was not found in the local directory, request it from the client
			// and write the CustomFile object retaining to it
			if (!found) {
				output.writeUTF("FILE");
				ObjectOutputStream out = new ObjectOutputStream(output);
				out.writeObject(foriegnFile);
			}
		}
		
		// Scan for subdirectories and add them
		for(Directory foreignDirec : foriegn.getDirectories()) {
			boolean found = false;
			// Search if the subdirectory is already present
			for (Directory localDirecs : local.getDirectories()) {
				// If it is present the recurse through and request for missing and updated files
				if (localDirecs.getRelativePath().equals(foreignDirec.getRelativePath())) {
					compareDirectories(foreignDirec, localDirecs);
					found = true;
					break;
				}
			}
			
			// If the subdirectory is not found, then make the subdirectory and recurse
			if (!found) {
				File dir = new File(homeDirectory + foreignDirec.getRelativePath());
				dir.mkdirs();
				Directory newDirec = new Directory(Paths.get(homeDirectory + foreignDirec.getRelativePath()), foreignDirec.getRelativePath());
				newDirec.scan();
				compareDirectories(foreignDirec, newDirec);
			}
		}
	}
	
	public void run() 
	{
		DirectoryInfoFile directoryInfoFile = new DirectoryInfoFile();
		try {
			directoryInfoFile.getDirectoryFromInfoFile(pathToFile);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Directory foreign = directoryInfoFile.getDirectory();
		try {
			compareDirectories(foreign, directory);
			output.writeUTF("QUIT");
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
