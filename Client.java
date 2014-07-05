/*
 * Client logic for the code, credit for main ideas behind logic goes to: http://mrbool.com/file-transfer-between-2-computers-with-java/24516
 */
import java.util.Scanner;
import java.net.*;
import java.io.*;
public class Client {

	public static void main (String [] args ) throws IOException {
	    Scanner reader=new Scanner(System.in);
		int maxfilesize=1022386; 
	    int bytesRead;
	    int counter = 0;
	    String nameoffile;
	    Socket socket = new Socket("127.0.0.1",9600);
	    byte [] bytearray  = new byte [maxfilesize];
	    InputStream is = socket.getInputStream();
	    System.out.println("Please input the name of the file you wish the client to write to");
	    nameoffile=reader.nextLine();
	    FileOutputStream fos = new FileOutputStream(nameoffile+".txt");
	    BufferedOutputStream bufferstream = new BufferedOutputStream(fos);
	    bytesRead = is.read(bytearray,0,bytearray.length);
	    counter= bytesRead;

	    do {
	       bytesRead =
	          is.read(bytearray, counter, (bytearray.length-counter));
	       if(bytesRead >= 0) counter += bytesRead;
	    } while(bytesRead > -1);

	    bufferstream.write(bytearray, 0 , counter);
	    bufferstream.flush();
	    bufferstream.close();
	    socket.close();
	  }
}