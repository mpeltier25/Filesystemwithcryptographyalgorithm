import java.io.*;
import java.util.*;

/** Basic shell like program to bring everything together.
 */
public class Shell
{
    private static String[] helpInfo =
    {
        "help",
        "'quit' or 'exit'",
        "format",
        "create fname",
        "read fname",
        "write fname data",
        "delete fileName",
        "caesarshift fileName",
        "Homophonic fileName",
        "'ls' or 'dir'"
    };
    public static void main(String[] args)
    {
        if ( args.length > 1 )
        {
            System.err.println("usage: FileTester [ script-file ]");
            System.exit(0);
        }
        boolean fromFile = (args.length == 1);
        BufferedReader input = null;
        if ( fromFile )
        {
            try
            {
                input = new BufferedReader(new FileReader(args[0]));
            }
            catch ( FileNotFoundException e )
            {
                System.err.println("Error: Script file " + args[0] +
                        " not found.");
                System.exit(1);
            }
        }
        else
        {
            input = new BufferedReader(new InputStreamReader(System.in));
        }
        for ( ;; )
        {
            String cmd = null;
            try
            {
                if (  ! fromFile )
                {
                    pr("--> ");
                    System.out.flush();
                }
                String line = input.readLine();
                if ( line == null )
                {
                    return;
                }
                line = line.trim();
                if ( line.length() == 0 )
                {
                    continue;
                }
                if ( line.startsWith("//") )
                {
                    if ( fromFile )
                    {
                        pl(line);
                    }
                    continue;
                }
                if ( line.startsWith("/*") )
                {
                    continue;
                }
                if ( fromFile )
                {
                    pl("--> " + line);
                }
                StringTokenizer st = new StringTokenizer(line);
                cmd = st.nextToken();
                int result = 0;
                if ( cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("exit"))
                {
                    return;
                }
                else if ( cmd.equalsIgnoreCase("help") || cmd.equals("?") )
                {
                    help();
                    continue;
                }
                else if ( cmd.equalsIgnoreCase("format") )
                {
                    result = Library.format();
                }
                else if ( cmd.equalsIgnoreCase("create") )
                {
                    result = Library.create(st.nextToken());
                }
                else if ( cmd.equalsIgnoreCase("read") )
                {
                    String fname = st.nextToken();
                    result = Library.read(fname);
                }
                else if ( cmd.equalsIgnoreCase("write") )
                {
                    String fname = st.nextToken();
                    String data = st.nextToken();
                    while (st.hasMoreTokens()) {
						  data += " " + st.nextToken();
					}
                    result = Library.write(fname, data);
                }
                else if ( cmd.equalsIgnoreCase("delete") )
                {
                    String fileName = st.nextToken();
                    result = Library.delete(fileName);
                }
                else if ( cmd.equalsIgnoreCase("ls") || cmd.equalsIgnoreCase("dir"))
                {
					     String[] files = new String[512];
					result = Library.list();
                }
                else if(cmd.equalsIgnoreCase("caesarshift"))
                {
                	String fileName=st.nextToken();
                	result=Library.caesarshift(fileName);
                }
                else if (cmd.equalsIgnoreCase("homophonic"))
                {
                	String fileName=st.nextToken();
                	result=Library.Homophonicsubstitution(fileName);
                }
                else
                {
                    pl("unknown command");
                    continue;
                }
                if ( result < 0 )
                {
                    pl("*** System call failed:  " +
                            Library.errorMessage[result * -1]);
                }
                else if ( result > 0 )
                {
                    pl("*** Result " + result + " from system call");
                }
            }
            catch ( NumberFormatException e )
            {
                pl("Invalid argument: " + e);
            }
            catch ( NoSuchElementException e )
            {
                pl("Incorrect number of arguments");
                help(cmd);
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                return;
            }
        } 
    }
    private static void help()
    {
        pl("Commands are:");
        for ( int i = 0; i < helpInfo.length; i ++ )
        {
            pl("    " + helpInfo[i]);
        }
    }
    private static void help(String cmd)
    {
        for ( int i = 0; i < helpInfo.length; i ++ )
        {
            if ( helpInfo[i].startsWith(cmd) )
            {
                pl("usage: " + helpInfo[i]);
                return;
            }
        }
        pl("unknown command '" + cmd + "'");
    } 
    private static void pl(Object o)
    {
        System.out.println(o);
    }
    private static void pr(Object o)
    {
        System.out.print(o);
    }
}