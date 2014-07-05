import java.io.*;
import java.math.BigDecimal;
import java.util.BitSet;

/** A software simulation of a Disk.
 */
public class Disksim implements Runnable {
    public static final int BLOCK_SIZE = 512;
    public final int DISK_SIZE;
    protected int currentBlock = 0;
    protected byte data[];
    protected boolean busy;
    private boolean isWriting;
    protected int targetBlock;
    private byte buffer[];
    private boolean requestQueued = false;
    protected int readCount;
    protected int writeCount;
    public static final int FILE_NAME_OFFSET = 32;
    public byte[] freeMap;
    public String[] fileTable;
    static protected class DiskException extends RuntimeException {
        public DiskException(String s) {
            super("*** YOU CRASHED THE DISK: " + s);
        }
    }
    public Disksim(int size) {
        File diskName = new File("DISK");
        if (diskName.exists()) {
            if (diskName.length() != size * BLOCK_SIZE) {
                throw new DiskException(
                    "File DISK exists but is the wrong size");
            }
        }
        
        this.DISK_SIZE = size;
        if (size < 1) {
            throw new DiskException("A disk must have at least one block!");
        }
        
        int mapSize = new BigDecimal(DISK_SIZE / 8.0).setScale(0, BigDecimal.ROUND_UP).intValue();
        freeMap = new byte[mapSize];
        fileTable = new String[size];
        data = new byte[DISK_SIZE * BLOCK_SIZE];
        int count = BLOCK_SIZE;
        
        boolean isNewDisk = false;
        try {
            FileInputStream is = new FileInputStream("DISK");
            is.read(data);
            System.out.println("Restored " + count + " bytes from file DISK");
            is.close();
        } catch (FileNotFoundException e) {
            System.out.println("Creating new disk");
            isNewDisk = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        if (isNewDisk) {
            createNewDisk();
        }
        else {
            loadDisk();
        }
    } 
    public void flush() {
        try {
            System.out.println("Saving contents to DISK file...");
            FileOutputStream out = new FileOutputStream("DISK");
            out.write(data);
            out.close();
            System.out.println(readCount + " read operations and "
                + writeCount + " write operations performed");
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    protected void delay(int targetBlock) {
        int sleepTime = 10 + Math.abs(targetBlock - currentBlock) / 5;
        try {
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void beginRead(int blockNumber, byte buffer[]) {
        if (
                blockNumber < 0
                || blockNumber >= DISK_SIZE
                || buffer == null
                || buffer.length < BLOCK_SIZE)
        {
            throw new DiskException("Illegal disk read request: "
                        + " block number " + blockNumber
                        + " buffer " + buffer);
        }

        if (busy) {
            throw new DiskException("Disk read attempted "
                        + " while the disk was still busy.");
        }

        isWriting = false;
        this.buffer = buffer;
        targetBlock = blockNumber;
        requestQueued = true;

        notify();
    } 
    public synchronized void beginWrite(int blockNumber, byte buffer[]) {
        if (
                blockNumber < 0
                || blockNumber >= DISK_SIZE
                || buffer == null
                || buffer.length < BLOCK_SIZE)
        {
            throw new DiskException("Illegal disk write request: "
                        + " block number " + blockNumber
                        + " buffer " + buffer);
        }

        if (busy) {
            throw new DiskException("Disk write attempted "
                        + " while the disk was still busy.");
        }

        isWriting = true;
        this.buffer = buffer;
        targetBlock = blockNumber;
        requestQueued = true;

        notify();
    } 
    protected synchronized void waitForRequest() {
        while(!requestQueued) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        requestQueued = false;
        busy = true;
    } 
    protected void finishOperation() {
        synchronized (this) {
            busy = false;
            currentBlock = targetBlock;
        }
        commandsim.interrupt(commandsim.INTERRUPT_DISK,
            0,0,null,null,null);
    }
    public void run() {
        for (;;) {
            waitForRequest();
            delay(targetBlock);
            if (isWriting) {
                System.arraycopy(
                    buffer, 0,
                    data, targetBlock * BLOCK_SIZE,
                    BLOCK_SIZE);
                writeCount++;
            } else {
                System.arraycopy(
                    data, targetBlock * BLOCK_SIZE,
                    buffer, 0,
                    BLOCK_SIZE);
                readCount++;
            }
            finishOperation();
        }
    }
    public void createNewDisk()
    {
        int bitsSwitched = 0;
        for (int i = 0; i < freeMap.length; i++) {
            byte myByte = freeMap[i];
            BitSet bits = Facilities.fromByte(myByte);
            for (int j = 0; j < 8; j++) {
                bits.set(j, true);
                if (++bitsSwitched >= freeMap.length) {
                    break;
                }
            }
            freeMap[i] = Facilities.toByteArray(bits)[bits.length() / 8];
            if (bitsSwitched >= freeMap.length) {
                break;
            }
        }
        System.arraycopy(freeMap, 0, data, 0, freeMap.length);
    }
    public void loadDisk()
    {
        System.arraycopy(data, 0, freeMap, 0, freeMap.length);
        for (int i = 0; i < DISK_SIZE; i++) {
            if (((freeMap[i / 8] >> (i % 8)) & 1) == 1) {
                int byteIndex = i * BLOCK_SIZE;
                byte[] fileName = new byte[FILE_NAME_OFFSET];
                for (int j = 0; j < fileName.length; j++) {
                    fileName[j] = data[byteIndex];
                    byteIndex++;
                }
                fileTable[i] = new String(fileName).trim();
            }
        }
    }
    public int getFileBlock(String fileName) {
        int index = -1;
        for (int i = 0; i < fileTable.length; i++) {
            String systemFile = fileTable[i];
            if (fileName.equals(systemFile)) {
                index = i;
            }
            if (index != -1) {
                break;
            }
        }
        return index;
    }
    public int getNextBlockIndex()
    {
        int index = -1;
        for (int i = 0; i < DISK_SIZE; i++) {
            if (((freeMap[i / 8] >> (i % 8)) & 1) == 0) {
                index = i;
            }
            if (index != -1) {
                break;
            }
        }
        return index;
    }
    public void setFreeMap(int blockIndex, boolean used)
    {
        byte usedByte = freeMap[blockIndex / 8];
        BitSet bits = Facilities.fromByte(usedByte);
        
        int usedIndex = blockIndex % 8;
        bits.set(usedIndex, used);
        
        freeMap[blockIndex / 8] = Facilities.toByteArray(bits)[bits.length() / 8];
        System.arraycopy(freeMap, 0, data, 0, freeMap.length);
    }
}