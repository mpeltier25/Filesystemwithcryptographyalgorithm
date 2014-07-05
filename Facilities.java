import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
public class Facilities
{
    public static final SimpleDateFormat m_dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static void pack(short n, byte[] buf, int offset)
    {
        buf[offset] = (byte) n;
        buf[offset + 1] = (byte) (n >> 8);
    }
    public static short unpackShort(byte[] buf, int offset)
    {
        return (short) ((buf[offset] & 0xff) + ((buf[offset + 1] & 0xff) << 8));
    }
    public static void pack(int n, byte[] buf, int offset)
    {
        buf[offset] = (byte) (n >> 0);
        buf[offset + 1] = (byte) (n >> 8);
        buf[offset + 2] = (byte) (n >> 16);
        buf[offset + 3] = (byte) (n >> 24);
    }
    public static int unpackInt(byte[] buf, int offset)
    {
        return ((buf[offset] & 0xff) + ((buf[offset + 1] & 0xff) << 8) +
                ((buf[offset + 2] & 0xff) << 16) + ((buf[offset + 3] & 0xff) <<
                24));
    }
    public static void pack(boolean[] bits, byte[] buf, int offset)
    {
        for ( int i = offset; i < offset + bits.length / 8 + 1; i ++ )
        {
            buf[i] = (byte) 0;
        }
        for ( int i = 0; i < bits.length; i ++ )
        {
            if ( bits[i] )
            {
                buf[offset + i / 8] |= 1 << (8 - (i % 8) - 1);
            }
        }
    }
    public static boolean[] unpackArrayBool(byte[] buf, int offset, int size)
    {
        boolean[] bits = new boolean[size];
        for ( int i = 0; i < size; i ++ )
        {
            if ( (buf[(offset + i / 8)] & (1 << (8 - (i % 8) - 1))) > 0 )
            {
                bits[i] = true;
            }

        }
        return bits;
    }
    public static void pack(String text, byte[] buf, int offset)
    {
        byte[] bytes = text.getBytes();
        for ( int i = 0; i < bytes.length; i ++ )
        {
            buf[offset + i] = bytes[i];
        }
    }
    public static String unpackString(byte[] buf, int offset)
    {
        return new String(buf, offset, buf.length - offset - 1);
    }
    public static String strRepeat(String string, int count)
    {
        String result = "";
        for ( int i = 0; i < count; i ++ )
        {
            result = result + string;
        }
        return result;
    }
    public static String boolToStr(boolean[] bits)
    {
        String text = "";
        for ( int i = 0; i < bits.length; i ++ )
        {
            if ( bits[i] )
            {
                text += "1";
            }
            else
            {
                text += "0";
            }
        }
        return text;
    }
    public static BitSet fromByte(byte b) {  
        BitSet bits = new BitSet(8);  
        for (int i = 0; i < 8; i++) {  
            bits.set(i, (b & 1) == 1);  
            b >>= 1;  
        }  
        return bits;  
    }  
    public static byte[] toByteArray(BitSet bits) 
    {
        byte[] bytes = new byte[bits.length() / 8 + 1];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return bytes;
    }
    public static boolean isEmpty(String str)
    {
        boolean isEmpty = false;
        if (str.isEmpty() || "".equals(str)) {
            isEmpty = true;
        }
        else if (str.length() > 1) {
            char[] chars = str.toCharArray();
            boolean notEmpty = false;
            for (char character : chars) {
                if (!isEmpty(Character.toString(character))) {
                    notEmpty = true;
                }
            }
            isEmpty = !notEmpty;
        }
        return isEmpty;
    }
    public static void logMessage(String message)
    {
        OutputStream out = null;
        try {
            out = new FileOutputStream("log.txt", true);
            
            StringBuilder messageDetail = new StringBuilder();
            messageDetail.append(m_dateFormat.format(new Date()));
            messageDetail.append(": ");
            messageDetail.append(message);
            messageDetail.append("\n");
            
            out.write(messageDetail.toString().getBytes(), 0, messageDetail.length());
        }
        catch (FileNotFoundException e) { }
        catch (IOException e) { }
        finally {
            if (out != null)
            {
                try {
                    out.flush();
                    out.close();
                }
                catch (IOException e) {
                }
            }
        }
    }
}