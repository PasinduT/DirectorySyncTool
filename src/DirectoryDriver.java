import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

public class DirectoryDriver 
{
	public static void main(String [] args)
	{
		try {
			// Make a
			DirectoryInfoFile directoryInfoFile = new DirectoryInfoFile();
			directoryInfoFile.makeDirectoryInfoFile("direcInfo.ser", Paths.get("/Users/pasindutennakoon/Desktop/CS250"));
			//directoryInfoFile.sendDirectoryFile("localhost", 4999, "direcInfo.ser");
			
			DirectoryInfoFile directoryInfoFile2 = new DirectoryInfoFile();
			directoryInfoFile2.getDirectoryFromInfoFile("direcInfo.ser");
			
			System.out.println(directoryInfoFile2.getDirectory().getDirectory(0).getFile(0).getRelativePath());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
