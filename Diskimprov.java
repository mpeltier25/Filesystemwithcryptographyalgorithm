/** A new and improved Disk.
 */
public class Diskimprov extends Disksim {
    
    public Diskimprov(int size) {
        super(size);
        if (size < 0 || size >= (1<<15)) {
            throw new DiskException(
                String.format(
                    "Cannot make a improved Disk with %d blocks.  Max size is %d.",
                    size, 1<<15));
        }
        
    }

    /** Performs a read operation.
     */
    public void read(int blockNumber, byte buffer[]) {
        System.arraycopy(
            data, (blockNumber * BLOCK_SIZE) + FILE_NAME_OFFSET,
            buffer, 0,
            BLOCK_SIZE - FILE_NAME_OFFSET);
        readCount++;
    }

    /** Performs a write operation.
     */
    public void write(int blockNumber, byte buffer[], boolean isNewFile) {
        System.arraycopy(
            buffer, 0,
            data, (blockNumber * BLOCK_SIZE) + 
            (isNewFile ? 0 : FILE_NAME_OFFSET),
            BLOCK_SIZE - FILE_NAME_OFFSET);
        writeCount++;
    }
}