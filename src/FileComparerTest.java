import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class FileComparerTest {

	@Test
	void testRun() {
		
		try {
			ServerSocket server = new ServerSocket(5003);
			System.out.println("hello");
			
			
			Socket other = new Socket("localhost", 5003);
			Socket socket = server.accept();
			FileComparer comp = new FileComparer("/Users/pasindutennakoon/Desktop/testing", socket, "/Users/pasindutennakoon/Desktop/testing1.ser", "/Users/pasindutennakoon/Desktop/testing.ser");
			comp.start();
			comp.join();
			assert(Files.exists(Paths.get("/Users/pasindutennakoon/Desktop/testing.ser")));
			socket.close();
			server.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
