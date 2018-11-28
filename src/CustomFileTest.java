import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class CustomFileTest {

	@Test
	void testUpdate() throws IOException {
		CustomFile file = new CustomFile("/Users/pasindutennakoon/Desktop/javadocthing/overview-tree.html");
		file.update();
		assertEquals(file.getName(), "overview-tree.html");
	}

	@Test
	void testGetName() throws IOException {
		CustomFile file = new CustomFile("/Users/pasindutennakoon/Desktop/javadocthing/overview-tree.html");
		file.update();
		assertEquals(file.getName(), "overview-tree.html");
	}

	@Test
	void testGetRelativePath() throws IOException {
		CustomFile file = new CustomFile("/Users/pasindutennakoon/Desktop/javadocthing/overview-tree.html");
		file.update();
		assertEquals(file.getRelativePath(), "");
	}

	@Test
	void testGetPath() throws IOException {
		CustomFile file = new CustomFile("/Users/pasindutennakoon/Desktop/javadocthing/overview-tree.html");
		file.update();
		assertEquals(file.getPath(), Paths.get("/Users/pasindutennakoon/Desktop/javadocthing/overview-tree.html"));
	}

	@Test
	void testGetSize() throws IOException {
		CustomFile file = new CustomFile("/Users/pasindutennakoon/Desktop/javadocthing/overview-tree.html");
		file.update();
		assertEquals(file.getSize(), 3902);
	}

	@Test
	void testGetLastModified() throws IOException {
		CustomFile file = new CustomFile("/Users/pasindutennakoon/Desktop/javadocthing/overview-tree.html");
		file.update();
		assertEquals(file.getLastModified().toMillis(), 1539057135000L);
	}

}
