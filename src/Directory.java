import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Stores information needed about a directory
 * including: its name and path, subdirectories, and files directly underneath.
 * The class is also serializable so it can be stored in a file before transmitting across a network.
 * @author Pasindu Tennakoon
 * 13th of October 2018
 */
public class Directory implements Serializable{
	private ArrayList<Directory> directories;
	private ArrayList<CustomFile> files;
	private transient Path path;
	private String name;
	private String relativePath;
	
	/**
	 * Constructor
	 * @param path The actual path of the directory
	 * @param relativePath relative path from the root directory
	 */
	public Directory(Path path, String relativePath)
	{
		this.path = Paths.get(path.toUri());
		if (!Files.isDirectory(this.path))
			throw new IllegalArgumentException("The path should be a directory!");
		
		directories = new ArrayList<Directory>();
		files = new ArrayList<CustomFile>();
		this.name = this.path.getFileName().toString(); // Update the name
		this.relativePath = relativePath;
	}
	
	/**
	 * Constructor
	 * @param path The actual path of the directory
	 */
	public Directory(Path path)
	{
		this.path = Paths.get(path.toUri());
		if (!Files.isDirectory(this.path))
			throw new IllegalArgumentException("The path should be a directory!");
		
		directories = new ArrayList<Directory>();
		files = new ArrayList<CustomFile>();
		this.name = this.path.getFileName().toString(); // Update the name
		this.relativePath = "/";
	}
	
	/**
	 * Constructor
	 * @param path String representation of the path
	 */
	public Directory(String path)
	{
		this.path = Paths.get(path);
		if (!Files.isDirectory(this.path))
			throw new IllegalArgumentException("The path should be a directory!");
		
		directories = new ArrayList<Directory>();
		files = new ArrayList<CustomFile>();
		this.name = this.path.getFileName().toString(); // Update the name
		this.relativePath = "/";
	}
	
	/**
	 * Returns the list of directories stored inside this directory
	 * @return an ArrayList of directories
	 */
	public ArrayList<Directory> getDirectories()
	{
		return (ArrayList<Directory>) this.directories.clone();
	}
	
	/**
	 * Returns the list of files stored inside this directory
	 * @return an ArrayList of files represented by CustomFile objects
	 */
	public ArrayList<CustomFile> getFiles()
	{
		return (ArrayList<CustomFile>) this.files.clone();
	}
	
	/**
	 * Get a specific directory directly under the current directory
	 * @param index index of the directory in the ArrayList
	 * @return Directory object retaining information related the chosen directory
	 */
	public Directory getDirectory(int index)
	{
		if (index < 0 || index >= directories.size())
		{
			throw new IndexOutOfBoundsException("The index you specified was invalid");
		}
		return directories.get(index);
	}
	
	/**
	 * Get a specific file directly under the current directory
	 * @param index index of the file in the ArrayList
	 * @return CustomFile object retaining information related the chosen directory
	 */
	public CustomFile getFile(int index)
	{
		if (index < 0 || index >= files.size())
		{
			throw new IndexOutOfBoundsException("The index you specified was invalid");
		}
		return files.get(index);
	}
	
	/**
	 * Returns the relative path from the home directory
	 * @return String representation of the relative path
	 */
	public String getRelativePath()
	{
		return this.relativePath;
	}
	
	/**
	 * Returns the name of the current Directory
	 * @return name of the current directory as a string
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the path of the current directory
	 * @return Path object of the current directory
	 */
	public Path getPath()
	{
		return Paths.get(path.toUri());
	}
	
	/**
	 * Scans the directory for file and subdirectory information to repopulate the ArrayLists
	 * @throws IOException If some error (like file deleting) occurs while scanning this execption is thrown
	 */
	public void scan() throws IOException
	{	
		ArrayList<Directory> tempDirectory = new ArrayList<Directory>();
		ArrayList<CustomFile> tempFiles = new ArrayList<CustomFile>();
		Iterator<Path> childNodes = Files.list(path).iterator();
		
		while(childNodes.hasNext())
		{
			Path child = childNodes.next();
			if (Files.isDirectory(child))
			{
				Directory temp = new Directory(child, this.relativePath + child.getFileName().toString() + "/");
				temp.scan();
				tempDirectory.add(temp);
			}
			else
			{
				CustomFile temp = new CustomFile(child, this.relativePath + child.getFileName().toString());
				temp.update();
				tempFiles.add(temp);
			}
		}
		
		directories = tempDirectory;
		files = tempFiles;
	}
	
	/**
	 * Returns the entire directory structure as a tree
	 */
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<CustomFile> fileIterator = files.iterator();
		Iterator<Directory> directoryIterator = directories.iterator();
		
		stringBuilder.append(getName());
		
		while (fileIterator.hasNext())
		{
			CustomFile temp = fileIterator.next();
			stringBuilder.append("\n" + ((fileIterator.hasNext() || directoryIterator.hasNext()) ? "\u251c ": "\u2514 "));
			stringBuilder.append(temp);
		}
		
		
		while(directoryIterator.hasNext())
		{
			Directory temp = directoryIterator.next();
			stringBuilder.append("\n" + ((directoryIterator.hasNext()) ? "\u251c ": "\u2514 "));
			stringBuilder.append(temp.toString().replaceAll("\n", ((directoryIterator.hasNext()) ? "\n\u2502 ": "\n ")));
		}
		
		return stringBuilder.toString();
	}


	/**
	 * This function is used for serializing an object of this class. It helps recreate the Path instance field
	 * @param oos The ObjectOutputStream java uses to output the object
	 * @throws IOException if the file/stream writing to cannot be found or gets closed
	 */
	private void writeObject(ObjectOutputStream oos) throws IOException 
	{
        oos.defaultWriteObject();
        oos.writeObject(path.toUri());
    }
 
	/**
	 * This function is used for deserializing an object of this class. It recreates the Path instance field
	 * @param ois The ObjectInputStream java uses to input the object
	 * @throws IOException if the file/stream reading from cannot be found or gets closed
	 * @throws ClassNotFoundException when the stored class cannot be found during deserialization.
	 */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException 
    {
        ois.defaultReadObject();
        URI pathURI = (URI) ois.readObject();
        this.path = Paths.get(pathURI);
    }
}
