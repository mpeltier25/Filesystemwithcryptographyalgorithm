/*
 * Server side implementation of file transfer. Credit for the main ideas of code go to: http://mrbool.com/file-transfer-between-2-computers-with-java/24516
 */
import java.util.Scanner;
import java.net.*;
import java.io.*;
public class Server {

	 public static void main (String [] args ) throws IOException {
		 	Scanner reader=new Scanner(System.in);
		    ServerSocket serverSocket = new ServerSocket(9600);
		      Socket socket = serverSocket.accept();
		      String document;
		      System.out.println("Hello server, could you please specify the file that you wish to send to the client?");
		      document=reader.nextLine();
		      File tobetransfered = new File (document+".txt");
		      byte [] bytearray  = new byte [(int)tobetransfered.length()];
		      FileInputStream fileinput = new FileInputStream(tobetransfered);
		      BufferedInputStream bufferinput = new BufferedInputStream(fileinput);
		      bufferinput.read(bytearray,0,bytearray.length);
		      OutputStream outputsocket = socket.getOutputStream();
		      outputsocket.write(bytearray,0,bytearray.length);
		      outputsocket.flush();
		      socket.close();
		      System.out.println("File was sent successfully");
		    }
}