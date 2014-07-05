import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/** A command simulation
 */
public class commandsim {
    public static final int INTERRUPT_USER = 0;
    public static final int INTERRUPT_DISK = 1;
    public static final int INTERRUPT_POWER_ON = 2;
    public static final int SYSCALL_OUTPUT = 0;
    public static final int SYSCALL_INPUT = 1;
    public static final int SYSCALL_EXEC = 2;
    public static final int SYSCALL_JOIN = 3;
    public static final int SYS_CALL_FORMAT_DISK=4;
    public static final int SYS_CALL_CREATE = 5;
    public static final int SYS_CALL_READ = 6;
    public static final int SYS_CALL_WRITE = 7;
    public static final int SYS_CALL_DELETE = 8;
    public static final int SYS_CALL_DIR = 9;
    public static final int ERROR_BAD_ARGUMENT = -1;
    public static final int ERROR_NO_CLASS = -2;
    public static final int ERROR_NO_MAIN = -3;
    public static final int ERROR_BAD_COMMAND = -4;
    public static final int ERROR_OUT_OF_RANGE = -5;
    public static final int ERROR_END_OF_FILE = -6;
    public static final int ERROR_IO = -7;
    public static final int ERROR_IN_CHILD = -8;
    public static final int ERROR_NO_SUCH_PROCESS = -9;
    private static Diskimprov disk;
    private static int cacheSize;
    public static int interrupt(int kind, int i1, int i2,
            Object o1, Object o2, byte[] a)
    {
        try {
            switch (kind) {
            case INTERRUPT_USER:
                switch (i1) {
                case SYSCALL_OUTPUT:
                    return doOutput((String)o1);

                case SYSCALL_INPUT:
                    return doInput((StringBuffer)o1);

                case SYSCALL_EXEC:
                    return doExec((String)o1,(String[])o2);

                case SYSCALL_JOIN:
                    return doJoin(i2);
                
                case SYS_CALL_CREATE:
                	return doCreate((String)o1);
                
                case SYS_CALL_FORMAT_DISK:
                	return doFormat();
                	
                case SYS_CALL_READ:
                	return doRead((String)o1, a);
                case SYS_CALL_WRITE:
                	return doWrite((String)o1,a);
                case SYS_CALL_DELETE:
                	return doDelete((String)o1);
                case SYS_CALL_DIR:
                	return doDir();
                	
                	
                default:
                    return ERROR_BAD_ARGUMENT;
                }

            case INTERRUPT_DISK:
                break;

            case INTERRUPT_POWER_ON:
                doPowerOn(i1, o1, o2);
                doShutdown();
                break;

            default:
                return ERROR_BAD_ARGUMENT;
            } 
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_BAD_ARGUMENT;
        }
        return 0;
    } 
    private static void doPowerOn(int i1, Object o1, Object o2) {
        cacheSize = i1;
        disk = (Diskimprov)o1;
        String shellCommand = (String) o2;

        doOutput("Kernel: Disk is " + disk.DISK_SIZE + " blocks\n");
        doOutput("Kernel: Disk cache size is " + i1 + " blocks\n");
        doOutput("Kernel: Loading initial program.\n");

        StringTokenizer st = new StringTokenizer(shellCommand);
        int n = st.countTokens();
        if (n < 1) {
            doOutput("Kernel: No shell specified\n");
            System.exit(1);
        }
            
        String shellName = st.nextToken();
        String[] args = new String[n - 1];
        for (int i = 1; i < n; i++) {
            args[i - 1] = st.nextToken();
        }

        if (doExecAndWait(shellName, args) < 0) {
            doOutput("Kernel: Unable to start " + shellCommand + "!\n");
            System.exit(1);
        } else {
            doOutput("Kernel: " + shellCommand + " has terminated.\n");
        }

        Launcher.joinAll();
    } 
    private static void doShutdown() {
        disk.flush();
    } 
    private static int doOutput(String msg) {
        System.out.print(msg);
        return 0;
    }
    private static BufferedReader br
        = new BufferedReader(new InputStreamReader(System.in));
    private static int doInput(StringBuffer sb) {
        try {
            String s = br.readLine();
            if (s==null) {
                return ERROR_END_OF_FILE;
            }
            sb.append(s);
            return 0;
        } catch (IOException t) {
            t.printStackTrace();
            return ERROR_IO;
        }
    } 
    private static int doExecAndWait(String command, String args[]) {
        Launcher l;
        try {
            l = new Launcher(command, args);
        } catch (ClassNotFoundException e) {
            return ERROR_NO_CLASS;
        } catch (NoSuchMethodException e) {
            return ERROR_NO_MAIN;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_BAD_COMMAND;
        }
        try {
            l.run();
            l.delete();
            return l.returnCode;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_IN_CHILD;
        }
    } 
    private static int doExec(String command, String args[]) {
        try {
            Launcher l = new Launcher(command, args);
            l.start();
            return l.pid.intValue();
        } catch (ClassNotFoundException e) {
            return ERROR_NO_CLASS;
        } catch (NoSuchMethodException e) {
            return ERROR_NO_MAIN;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_BAD_COMMAND;
        }
    }
    private static int doJoin(int pid) {
        return Launcher.joinOne(pid);
    }
    private static int doFormat(){
    	int diskSize = disk.DISK_SIZE;
    	byte[] freeMap = new byte[diskSize];
    	boolean[] freeMapBool = new boolean[diskSize];
    	String fileMapString = "";
    	byte[] fileMapBytes = new byte[512];
    	int[] blocksChanged = new int[2];
    	blocksChanged[0] = 0;
    	blocksChanged[1] = 1;
    	String[] descriptions = new String[2];
    	descriptions[0] = "Free map initialized, blocks 1 and 2 permanently allocated for the free map and the file map";
    	descriptions[1] = "File map initialized";
    	try{
    		BufferedWriter out = new BufferedWriter(new FileWriter("log.txt",true));
    		String bla = "Formatting the Disk ...\n";
    		out.append(bla);
    		out.close();
    	}
    	catch(IOException e){System.err.println(e);}
    	doLog(blocksChanged, descriptions);
    	
    	for(boolean free : freeMapBool){
    		free = false;
    	}
    	freeMapBool[0] = true;
    	freeMapBool[1] = true;
    	Facilities.pack(freeMapBool,freeMap,0);
    	Facilities.pack(fileMapString,fileMapBytes,0);
    	
    	disk.write(0,freeMap, true);
    	disk.write(1,fileMapBytes, true);
    	disk.setFreeMap(0, true);
    	disk.setFreeMap(1,true);
    	return 0;
    }
    private static int doCreate(String filename){
    	File f = new File(filename);
    	int x = disk.getNextBlockIndex();
    	disk.write(x,null,true);
    	disk.setFreeMap(x,true);
    	return 0;
    }
    private static int doRead(String filename,byte[] a){
    	disk.read(disk.getFileBlock(filename),a);
    	return 0;
    }
    private static int doWrite(String filename,byte[] buffer){
    	disk.write(disk.getFileBlock(filename),buffer,false);
    	return 0;
    }
    private static int doDelete(String filename){
    	disk.setFreeMap(disk.getFileBlock(filename),false);
    	return 0;
    }
    private static int doDir(){
    	disk.loadDisk();
    	return 0;
    }
    private static void doLog(int[] changedBlocks,String[] descriptions){
    	try{
    		BufferedWriter out = new BufferedWriter(new FileWriter("log.txt",true));
    		String output = String.format("Block#|Changed/Unchanged|Change Made\n");
    		out.append(output);
    	out.append("--------------------------------------------------------\n");
    	int j = 0 ;
    		for(int i = 0;i<11;i++){
    			if(j < descriptions.length&& i == changedBlocks[j]){
    				String bla = String.format("%7d|%17s|%4s\n",i,"Changed",descriptions[j]);
    				out.append(bla);
    				j++;
    					}
    			else{
    				String bla = String.format("%7d|%17s|%4s\n",i,"Unchanged","");
    				out.append(bla);
    					}
    			}
    		out.append("\n");
    		out.close();
    		} catch(IOException e){System.out.println("Something went wrong");}
    	} 
    static private class Launcher extends Thread {
        static Map pidMap = new HashMap();
        static private int nextpid = 1;
        private Method method;
        private Object arglist[];
        private Integer pid;
        private int returnCode = 0;
        public Launcher(String command, String args[])
                throws ClassNotFoundException, NoSuchMethodException
        {
            if (args==null) {
                args = new String[0];
            }
            Class params[] = new Class[] { args.getClass() };
            Class programClass = Class.forName(command);
            method = programClass.getMethod("main",params); 
            arglist = new Object[] { args };

            pid = new Integer(nextpid++);
            synchronized (pidMap) {
                pidMap.put(pid, this);
            }
        }
        public void run() {
            try {
                method.invoke(null,arglist);
            } catch (InvocationTargetException e) {
                System.out.println("Kernel: User error:");
                e.getTargetException().printStackTrace();

                returnCode = ERROR_IN_CHILD;
            } catch (Exception e) {
                System.out.println("Kernel: " + e);
                returnCode = ERROR_IN_CHILD;
            }
        }
        static public void joinAll() {
            for (Iterator e = pidMap.keySet().iterator(); e.hasNext(); ){
                Launcher l = (Launcher)e.next();
                try {
                    l.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    System.out.println("Kernel: join: " + ex);
                }
            }
        }
        static public int joinOne(int pid) {
            Object o;
            synchronized (pidMap) {
                o = pidMap.remove(new Integer(pid));
            }
            if (o == null) {
                return ERROR_NO_SUCH_PROCESS;
            }
            Launcher l = (Launcher)o;
            try {
                l.join();
            } catch (InterruptedException e) {
                System.out.println("Kernel: join: " + e);
            }
            return l.returnCode;
        }
        public void delete() {
            synchronized (pidMap) {
                pidMap.remove(pid);
            }
        }
     
    }
} 