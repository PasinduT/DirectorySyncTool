import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class writes the directory structure to a file and retrieves it from the file
 * In addition, the class has methods which can create the directory tree by scanning the directory
 * @author Pasindu Tennakoon
 */
public class DirectoryInfoFile {
	private Directory directory;
	
	/**
	 * Constructor
	 */
	public DirectoryInfoFile()
	{
		directory = null;
	}
	
	
	/**
	 * Make the directory information file
	 * @param filename The string representation of the path of the file where the directory information is stored into
	 * @param homeDirectory The path of the directory which needs to be scanned
	 * @throws IOException When the file could not be made or the home directory could not be found
	 */
	public void makeDirectoryInfoFile(String filename, Path homeDirectory) throws IOException
	{
		directory = new Directory(homeDirectory);
		directory.scan();
		
		FileOutputStream file = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(file);
		
		out.writeObject(directory);
		
		out.close();
		file.close();
	}
	
	/**
	 * Create the directory object by scanning the file directory
	 * @param pathToFile The string representation of the path of the file where the directory information is read from
	 * @throws IOException When the file could not be made or the home directory could not be found 
	 * @throws ClassNotFoundException When the Directory or CustomFile class could not be found in the program
	 */
	public void getDirectoryFromInfoFile(String pathToFile) throws IOException, ClassNotFoundException
	{
		FileInputStream file = new FileInputStream(pathToFile);
		ObjectInputStream in = new ObjectInputStream(file);
		directory = (Directory) in.readObject();
		
		in.close();
		file.close();
	}
	
	public void sendDirectoryFile(String ipAddrTo, int port, String pathToFile) throws UnknownHostException, IOException 
	{
		FileUploader fileUploader = new FileUploader("DIRECINFO", pathToFile, ipAddrTo, port);
		fileUploader.start();
	}
	
	/**
	 * Gets the Directory object scanned or read from a file
	 * @return Directory object scanned or read from a file
	 */
	public Directory getDirectory()
	{
		return directory;
	}

}
