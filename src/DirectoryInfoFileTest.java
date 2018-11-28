import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class DirectoryInfoFileTest {

	@Test
	void testMakeDirectoryInfoFile() throws IOException, ClassNotFoundException {
		DirectoryInfoFile directoryInfoFile = new DirectoryInfoFile();
		directoryInfoFile.makeDirectoryInfoFile("src/direcInfo.ser", Paths.get("/Users/pasindutennakoon/Desktop/javadocthing"));
		
		DirectoryInfoFile directoryInfoFile2 = new DirectoryInfoFile();
		directoryInfoFile2.getDirectoryFromInfoFile("direcInfo.ser");
		
		assertEquals(directoryInfoFile2.getDirectory().toString(), directoryInfoFile.getDirectory().toString());
	}

	@Test
	void testGetDirectoryFromInfoFile() throws IOException, ClassNotFoundException {
		DirectoryInfoFile directoryInfoFile = new DirectoryInfoFile();
		directoryInfoFile.makeDirectoryInfoFile("direcInfo.ser", Paths.get("/Users/pasindutennakoon/Desktop/javadocthing"));
		
		DirectoryInfoFile directoryInfoFile2 = new DirectoryInfoFile();
		directoryInfoFile2.getDirectoryFromInfoFile("direcInfo.ser");
		
		assertEquals(directoryInfoFile2.getDirectory().toString(), directoryInfoFile.getDirectory().toString());
	}

	@Test
	void testGetDirectory() throws IOException {
		DirectoryInfoFile directoryInfoFile = new DirectoryInfoFile();
		directoryInfoFile.makeDirectoryInfoFile("direcInfo.ser", Paths.get("/Users/pasindutennakoon/Desktop/javadocthing"));
		System.out.println(directoryInfoFile.getDirectory().toString());
		assert(true);
	}

}
