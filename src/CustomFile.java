import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

public class CustomFile implements Serializable, Comparable<CustomFile>
{
	private transient Path path;
	private long size;
	private String name;
	private String relativePath;
	private long lastModified;
	
	/**
	 * Constructor
	 * @param path Actual path of the file
	 * @throws IOException If the path doesn't exist
	 */
	public CustomFile(Path path) throws IOException
	{
		this.path = path;
		if (! Files.exists(path))
			throw new IOException("File \"" + path + "\" could not be found!");
		
		this.relativePath = "";
	}
	
	
	/**
	 * Constructor
	 * @param path Actual path of the file
	 * @param relativePath Relative path of the file from the root
	 * @throws IOException If the path doesn't exist
	 */
	public CustomFile(Path path, String relativePath) throws IOException
	{
		this.path = path;
		if (! Files.exists(path))
			throw new IOException("File \"" + path + "\" could not be found!");
		
		this.relativePath = relativePath;
	}
	

	/**
	 * Constructor
	 * @param path String representation of the path of the file
	 * @throws IOException If the path doesn't exist
	 */
	public CustomFile(String path) throws IOException
	{
		this.path = Paths.get(path);
		if (! Files.exists(this.path))
			throw new IOException("File \"" + path + "\" could not be found!");
		
		this.relativePath = "";
	}
	
	/**
	 * Updates the file's information
	 * @throws IOException if the file has been renamed or deleted
	 */
	public void update() throws IOException
	{
		size = Files.size(path);
		name = path.getFileName().toString();
		lastModified = Files.getLastModifiedTime(path).toMillis();
	}
	
	/**
	 * Returns the filename
	 * @return filename as a String
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the relative path from the root folder
	 * @return String representation from the root folder
	 */
	public String getRelativePath()
	{
		return relativePath;
	}
	
	/**
	 * Returns the Path object relating to the file
	 * @return Path object relating to the file
	 */
	public Path getPath()
	{
		return Paths.get(path.toUri());
	}
	
	/**
	 * Returns the size of the file
	 * @return long value of the filesize
	 */
	public long getSize()
	{
		return size;
	}
	
	/**
	 * Returns the filename
	 * @return String of the filename
	 */
	public String toString()
	{
		return getName();
	}
	
	
	/**
	 * Compares one file with another file
	 * @param other the second file to compare (this) file with
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @return returns an int value such that if it is negative, either the files do not match 
	 * or (this) file is made earlier than the (other) file. If the number is positive, then (this)
	 * file is made later than the (other) file. If it is zero, then the files have the
	 * same relative path and the same lastModifiedTime. 
	 * NOTE that this does not imply that the files are the same 
	 */
	public int compareTo(CustomFile other)
	{
		//Only if the files have the same path name check their timestamps
		if (this.getRelativePath().equals(other.getRelativePath())) {
			return this.getLastModified().compareTo(other.getLastModified());
		} else {
			return -1;
		}
	}
	
	/**
	 * Returns the last modified date
	 * @return FileTime object containing the last modified date of the file
	 */
	public FileTime getLastModified()
	{
		return FileTime.fromMillis(lastModified);
	}
	
	/**
	 * This function helps in serializing the object, by allowing the path variable to be constructed
	 * @param oos ObjectOutputStream where the object is being serialized in to
	 * @throws IOException when the stream disappears or looses connection
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
