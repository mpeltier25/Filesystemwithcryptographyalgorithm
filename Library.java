
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
public class Library {
    private Library() {}
    public static final String[] errorMessage = {
        "OK", // 0
        "Invalid argument",             // ERROR_BAD_ARGUMENT = -1
        "No such class",                // ERROR_NO_CLASS = -2
        "Class has no main method",     // ERROR_NO_MAIN = -3
        "Command aborted",              // ERROR_BAD_COMMAND = -4
        "Argument out of range",        // ERROR_OUT_OF_RANGE = -5
        "End of file on console input", // ERROR_END_OF_FILE = -6
        "I/O error on console input",   // ERROR_IO = -7
        "Exception in user program",    // ERROR_IN_CHILD = -8
        "No such process"               // ERROR_NO_SUCH_PROCESS = -9
    };
    public static int output(String s) {
        return commandsim.interrupt(commandsim.INTERRUPT_USER,
            commandsim.SYSCALL_OUTPUT, 0, s, null, null);
    }
    public static int input(StringBuffer result) {
        result.setLength(0);
        return commandsim.interrupt(commandsim.INTERRUPT_USER,
                            commandsim.SYSCALL_INPUT, 0, result, null, null);
    }
    public static int exec(String command, String args[]) {
        return commandsim.interrupt(commandsim.INTERRUPT_USER,
            commandsim.SYSCALL_EXEC, 0, command, args, null);
    }
    public static int join(int pid) {
        return commandsim.interrupt(commandsim.INTERRUPT_USER,
            commandsim.SYSCALL_JOIN, pid, null, null, null);
    } 
    public static int format() {
       File filedirectory= new File ("filedirectory"); filedirectory.mkdir();
       if (!filedirectory.exists()) {
           String message = filedirectory + " does not exist";
           return -1;
       }
       if (!filedirectory.isDirectory()) {
           String message = filedirectory + " is not a directory";
           return -1;
       }
       File[] filesindir = filedirectory.listFiles();
       for (int i = 0; i < filesindir.length; i++) {
           filesindir[i].delete();
       }
    	return 0;
    } 
    public static int create(String fname) {
    	File createfile = new File("filedirectory" + File.separator + ""+fname);
    	createfile.getParentFile().mkdirs();
    	try {
			createfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
    	return 0;
    } 
    public static int read(String fname){
    FileInputStream fstream;
	try {
		fstream = new FileInputStream("filedirectory" + File.separator + ""+fname);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return -1;
	}
    DataInputStream ds = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(ds));
    String line;
    try {
		while ((line = br.readLine()) != null)   {
		System.out.println (line);
		}
	} catch (IOException e) {
		e.printStackTrace();
		return -1;
	}
    try {
		ds.close();
	} catch (IOException e) {
		e.printStackTrace();
		return -1;
	}
    return 0;
    }
    public static int write(String fname, String buffer){
    	PrintWriter outputFile;
		try {
			outputFile = new PrintWriter("filedirectory" + File.separator + ""+fname);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
    	outputFile.println(""+buffer);
    	outputFile.close();
    	return 0;	
    } 
    public static int list(){
    	File filedirectory= new File ("filedirectory"); filedirectory.mkdir();
        if (!filedirectory.exists()) {
            String message = filedirectory + " does not exist";
            return -1;
        }
        if (!filedirectory.isDirectory()) {
            String message = filedirectory + " is not a directory";
            return -1;
        }
        File[] files = filedirectory.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].getName();
            System.out.println(""+files[i].getName());
        }
    	return 0;
    }
    public static int delete(String fname){
    	File deletefile = new File("filedirectory" + File.separator + ""+fname);
    	deletefile.getParentFile().mkdirs();
    	if (!deletefile.exists()) {
            String message = deletefile + " does not exist";
            return -1;
        }
    	deletefile.delete();
    	return 0;
    }
    public static int caesarshift(String fname)
    {
    Scanner reader=new Scanner(System.in);
    	FileInputStream fstream;
    	try {
    		fstream = new FileInputStream("filedirectory" + File.separator + ""+fname);
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    		return -1;
    	}
        DataInputStream ds = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(ds));
        String line;
        try {
    		while ((line = br.readLine()) != null)   {
    		String question;
    		String filename;
    		System.out.println (line);
    		String caesarString = line.replaceAll("[aA]","D");
    		String caesarString2=caesarString.replaceAll("[bB]", "E");
    		String caesarString3=caesarString2.replaceAll("[cC]", "F");
    		String caesarString4=caesarString3.replaceAll("[dD]","G");
    		String caesarString5=caesarString4.replaceAll("[eE]", "H");
    		String caesarString6=caesarString5.replaceAll("[fF]", "I");
    		String caesarString7=caesarString6.replaceAll("[gG]", "J");
    		String caesarString8=caesarString7.replaceAll("[hH]", "K");
    		String caesarString9=caesarString8.replaceAll("[iI]", "L");
    		String caesarString10=caesarString9.replaceAll("[jJ]", "M");
    		String caesarString11=caesarString10.replaceAll("[kK]", "O");
    		String caesarString12=caesarString11.replaceAll("[lL]", "P");
    		String caesarString13=caesarString12.replaceAll("[mM]", "Q");
    		String caesarString14=caesarString13.replaceAll("[oO]", "R");
    		String caesarString15=caesarString14.replaceAll("[pP]", "S");
    		String caesarString16=caesarString15.replaceAll("[qQ]", "T");
    		String caesarString17=caesarString16.replaceAll("[rR]", "U");
    		String caesarString18=caesarString17.replaceAll("[sS]", "V");
    		String caesarString19=caesarString18.replaceAll("[tT]", "W");
    		String caesarString20=caesarString19.replaceAll("[uU]", "X");
    		String caesarString21=caesarString20.replaceAll("[vV]", "Y");
    		String caesarString22=caesarString21.replaceAll("[wW]", "Z");
    		String caesarString23=caesarString22.replaceAll("[xX]", "A");
    		String caesarString24=caesarString23.replaceAll("[yY]", "B");
    		String caesarString25=caesarString24.replaceAll("[zZ]", "C");
    		System.out.println("Here is the string converted into caesar's shift: " +caesarString25);
    		System.out.println("Would you like to create a seperate file with the encryption?");
    		question=reader.nextLine();
    		if(question.equalsIgnoreCase("yes"))
    		{
    		System.out.println("Please specify the name of the file.");
    		filename=reader.nextLine();
    		File createfile = new File("filedirectory" + File.separator + ""+filename);
        	createfile.getParentFile().mkdirs();
        	try {
    			createfile.createNewFile();
    		} catch (IOException e) {
    			e.printStackTrace();
    			return -1;
    		}
        	PrintWriter outputFile;
    		try {
    			outputFile = new PrintWriter("filedirectory" + File.separator + ""+filename);
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    			return -1;
    		}
        	outputFile.println(""+caesarString25);
        	outputFile.close();
    		}
    		else if(question.equalsIgnoreCase("no"))
    		{
    		System.out.println("Thank you for using caesar's shift");
    		return 0;
    		}
    		else 
    		{
    		System.out.println("Input invalid, I need a yes or no response. Restarting method");
    		caesarshift(fname);
    		}
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    		return -1;
    	}
        try {
    		ds.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return -1;
    	}
	return 0;
    }
    public static int Homophonicsubstitution(String fname)
    {
    	Scanner reader=new Scanner(System.in);
    	FileInputStream fstream;
    	String[] arraya={"46", "52", "58", "79", "91"};
		String[] arrayb={"04", "20"};
		String[] arrayd={"14", "97", "69", "96"};
		String[] arraye={"09", "31", "39", "50", "65"};
		String[] arrayf={"48", "73"};
		String[] arrayg={"74", "85"};
		String[] arrayh={"36", "37", "61", "68"};
		String[] arrayi={"13", "18", "47", "70"};
		String[] arrayj={"10", "38"};
		String[] arrayl={"24", "29", "49"};
		String[] arraym={"15", "60"};
		String[] arrayn={"07", "23", "54"};
		String[] arrayo={"22", "63", "41"};
		String[] arrayp={"76", "95"};
		String[] arrayr={"08", "34", "42", "53"};
		String[] arrays={"12", "27", "64"};
		String[] arrayt={"01", "19", "35", "78", "93"};
		String[] arrayu={"17", "32"};
		String[] arrayy={"67", "71"};
		Random randomnumbergeneratora=new Random();
		int randomnumbera=randomnumbergeneratora.nextInt(5);
		Random randomnumbergeneratorb=new Random();
		int randomnumberb=randomnumbergeneratorb.nextInt(2);
		Random randomnumbergeneratord=new Random();
		int randomnumberd=randomnumbergeneratord.nextInt(4);
		Random randomnumbergeneratore=new Random();
		int randomnumbere=randomnumbergeneratore.nextInt(5);
		Random randomnumbergeneratorf=new Random();
		int randomnumberf=randomnumbergeneratorf.nextInt(2);
		Random randomnumbergeneratorg=new Random();
		int randomnumberg=randomnumbergeneratorg.nextInt(2);
		Random randomnumbergeneratorh=new Random();
		int randomnumberh=randomnumbergeneratorh.nextInt(4);
		Random randomnumbergeneratori=new Random();
		int randomnumberi=randomnumbergeneratori.nextInt(4);
		Random randomnumbergeneratorj=new Random();
		int randomnumberj=randomnumbergeneratorj.nextInt(2);
		Random randomnumbergeneratorl=new Random();
		int randomnumberl=randomnumbergeneratorl.nextInt(3);
		Random randomnumbergeneratorm=new Random();
		int randomnumberm=randomnumbergeneratorm.nextInt(2);
		Random randomnumbergeneratorn=new Random();
		int randomnumbern=randomnumbergeneratorn.nextInt(3);
		Random randomnumbergeneratoro=new Random();
		int randomnumbero=randomnumbergeneratoro.nextInt(3);
		Random randomnumbergeneratorp=new Random();
		int randomnumberp=randomnumbergeneratorp.nextInt(2);
		Random randomnumbergeneratorr=new Random();
		int randomnumberr=randomnumbergeneratorr.nextInt(4);
		Random randomnumbergenerators=new Random();
		int randomnumbers=randomnumbergenerators.nextInt(3);
		Random randomnumbergeneratort=new Random();
		int randomnumbert=randomnumbergeneratort.nextInt(5);
		Random randomnumbergeneratoru=new Random();
		int randomnumberu=randomnumbergeneratoru.nextInt(2);
		Random randomnumbergeneratory=new Random();
		int randomnumbery=randomnumbergeneratory.nextInt(2);
    	try {
    		fstream = new FileInputStream("filedirectory" + File.separator + ""+fname);
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    		return -1;
    	}
        DataInputStream ds = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(ds));
        String line;
        try {
    		while ((line = br.readLine()) != null)   {
    		String question;
    		String filename;
    		System.out.println (line);
    		String Homophonicsub1=line.replaceAll("[aA]", ""+arraya[randomnumbera]);
    	    String Homophonicsub2=Homophonicsub1.replaceAll("[bB]", ""+arrayb[randomnumberb]);
    	    String Homophonicsub3=Homophonicsub2.replaceAll("[cC]", "55");
    	    String Homophonicsub4=Homophonicsub3.replaceAll("[dD]", ""+arrayd[randomnumberd]);
    	    String Homophonicsub5=Homophonicsub4.replaceAll("[eE]", ""+arraye[randomnumbere]);
    	    String Homophonicsub6=Homophonicsub5.replaceAll("[fF]", ""+arrayf[randomnumberf]);
    	    String Homophonicsub7=Homophonicsub6.replaceAll("[gG]", ""+arrayg[randomnumberg]);
    	    String Homophonicsub8=Homophonicsub7.replaceAll("[hH]", ""+arrayh[randomnumberh]);
    	    String Homophonicsub9=Homophonicsub8.replaceAll("[iI]", ""+arrayi[randomnumberi]);
    	    String Homophonicsub10=Homophonicsub9.replaceAll("[jJ]", ""+arrayj[randomnumberj]);
    	    String Homophonicsub11=Homophonicsub10.replaceAll("[kK]", "16");
    	    String Homophonicsub12=Homophonicsub11.replaceAll("[lL]", ""+arrayl[randomnumberl]);
    	    String Homophonicsub13=Homophonicsub12.replaceAll("[mM]", ""+arraym[randomnumberm]);
    	    String Homophonicsub14=Homophonicsub13.replaceAll("[nN]", ""+arrayn[randomnumbern]);
    	    String Homophonicsub15=Homophonicsub14.replaceAll("[oO]", ""+arrayo[randomnumbero]);
    	    String Homophonicsub16=Homophonicsub15.replaceAll("[pP]", ""+arrayp[randomnumberp]);
    	    String Homophonicsub17=Homophonicsub16.replaceAll("[qQ]", "30");
    	    String Homophonicsub18=Homophonicsub17.replaceAll("[rR]", ""+arrayr[randomnumberr]);
    	    String Homophonicsub19=Homophonicsub18.replaceAll("[sS]", ""+arrays[randomnumbers]);
    	    String Homophonicsub20=Homophonicsub19.replaceAll("[tT]", ""+arrayt[randomnumbert]);
    	    String Homophonicsub21=Homophonicsub20.replaceAll("[uU]", ""+arrayu[randomnumberu]);
    	    String Homophonicsub22=Homophonicsub21.replaceAll("[vV]", "06");
    	    String Homophonicsub23=Homophonicsub22.replaceAll("[wW]", "66");
    	    String Homophonicsub24=Homophonicsub23.replaceAll("[xX]", "57");
    	    String Homophonicsub25=Homophonicsub24.replaceAll("[yY]", ""+arrayy[randomnumbery]);
    	    String Homophonicsub26=Homophonicsub25.replaceAll("[zZ]", "26");
    	    System.out.println("Here is the string converted into Homophonic substitution: " +Homophonicsub26);
    		System.out.println("Would you like to create a seperate file with the encryption?");
    		question=reader.nextLine();
    	    if(question.equalsIgnoreCase("yes"))
    		{
    		System.out.println("Please specify the name of the file.");
    		filename=reader.nextLine();
    		File createfile = new File("filedirectory" + File.separator + ""+filename);
        	createfile.getParentFile().mkdirs();
        	try {
    			createfile.createNewFile();
    		} catch (IOException e) {
    			e.printStackTrace();
    			return -1;
    		}
        	PrintWriter outputFile;
    		try {
    			outputFile = new PrintWriter("filedirectory" + File.separator + ""+filename);
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    			return -1;
    		}
        	outputFile.println(""+Homophonicsub26);
        	outputFile.close();
    		}
    		else if(question.equalsIgnoreCase("no"))
    		{
    		System.out.println("Thank you for using Homophonic substitution");
    		return 0;
    		}
    		else 
    		{
    		System.out.println("Input invalid, I need a yes or no response. Restarting method");
    		Homophonicsubstitution(fname);
    		}
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    		return -1;
    	}
        try {
    		ds.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return -1;
    	}
    return 0;
    }
}