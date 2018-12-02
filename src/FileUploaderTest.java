import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class FileUploaderTest {

	@Test
	void testRun() {
		try {
			ServerSocket server = new ServerSocket(5000);
			Socket other = new Socket("localhost", 5000);
			Socket socket = server.accept();
			FileDownloader download = new FileDownloader("/Users/pasindutennakoon/Desktop/something.ser", new DataInputStream(socket.getInputStream()));
			FileUploader fileUploader = new FileUploader("TESTING", "/Users/pasindutennakoon/Desktop/testing1.ser", "localhost", 5000);
			fileUploader.start();
			download.start();
			fileUploader.join();
			download.join();
			assert(Files.exists(Paths.get("/Users/pasindutennakoon/Desktop/something.ser")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
