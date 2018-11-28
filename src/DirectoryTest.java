import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class DirectoryTest {

	@Test
	void testGetDirectories() throws IOException {
		Directory test = new Directory("/Users/pasindutennakoon/Desktop/javadocthing");
		test.scan();
		System.out.println(test.getDirectories().toString());
		assert(true);
	}

	@Test
	void testGetFiles() throws IOException {
		Directory test = new Directory("/Users/pasindutennakoon/Desktop/javadocthing");
		test.scan();
		System.out.println(test.getFiles().toString());
		assert(true);
	}

	@Test
	void testGetDirectory() throws IOException {
		Directory test = new Directory("/Users/pasindutennakoon/Desktop/javadocthing");
		test.scan();
		assertEquals(test.getDirectory(0).toString(), "Assignment_4\n" + 
				"├ package-frame.html\n" + 
				"├ MessageQueue.html\n" + 
				"├ package-summary.html\n" + 
				"└ package-tree.html");
	}

	@Test
	void testGetFile() throws IOException {
		Directory test = new Directory("/Users/pasindutennakoon/Desktop/javadocthing");
		test.scan();
		assertEquals(test.getFile(1).toString(), "overview-tree.html");
	}

	@Test
	void testGetRelativePath() throws IOException {
		Directory test = new Directory("/Users/pasindutennakoon/Desktop/javadocthing");
		test.scan();
		assertEquals(test.getFile(1).getRelativePath(), "/overview-tree.html");
	}

	@Test
	void testGetName() throws IOException {
		Directory test = new Directory("/Users/pasindutennakoon/Desktop/javadocthing");
		test.scan();
		assertEquals(test.getDirectory(0).getName(), "Assignment_4");
	}

	@Test
	void testGetPath() throws IOException {
		Directory test = new Directory("/Users/pasindutennakoon/Desktop/javadocthing");
		test.scan();
		assertEquals(test.getDirectory(0).getPath().toString(), "/Users/pasindutennakoon/Desktop/javadocthing/Assignment_4");
	}

	@Test
	void testScan() throws IOException {
		Directory test = new Directory("/Users/pasindutennakoon/Desktop/javadocthing");
		test.scan();
		System.out.println(test.toString());
		assert(true);
	}


}
