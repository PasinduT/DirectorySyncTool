import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Paths;

public class FileComparer extends Thread implements Runnable{
	private Directory directory;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private String homeDirectory;
	private String pathToFile;
	
	public FileComparer(String homeDirectory, Socket socket, String pathToFile) throws IOException 
	{
		this.pathToFile = pathToFile;
		this.socket = socket;
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
		directory = new Directory(Paths.get(homeDirectory));
		this.homeDirectory = homeDirectory;
		directory.scan();
		
	}
	
	public void compareDirectories(Directory foriegn, Directory local) throws IOException {
		
		for(CustomFile foriegnFile : foriegn.getFiles()) {
			boolean found = false;
			for (CustomFile localFile : local.getFiles()) {
				int comp = foriegnFile.compareTo(localFile);
				if (comp == 0) {
					found = true;
					break;
				}
				else if (comp > 0) {
					output.writeUTF("FILE");
					ObjectOutputStream out = new ObjectOutputStream(output);
					out.writeObject(foriegnFile);
					found = true;
					break;
				}
			}
			
			if (!found) {
				output.writeUTF("FILE");
				ObjectOutputStream out = new ObjectOutputStream(output);
				out.writeObject(foriegnFile);
			}
		}
		
		// Scan for directories and add them
		for(Directory foreignDirec : foriegn.getDirectories()) {
			boolean found = false;
			for (Directory localDirecs : local.getDirectories()) {
				if (localDirecs.getRelativePath().equals(foreignDirec.getRelativePath())) {
					compareDirectories(foreignDirec, localDirecs);
					found = true;
					break;
				}
			}
			
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
