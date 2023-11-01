import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * class to represent a Buffer, 4 byte array storing KV pairs
 * @author pratc nchilakala
 * @version 11/1/2023
 * 
 */
public class Buffer {
	private byte[] val;
	private byte dirtyBit;
	private int blockIndex;
	private RandomAccessFile file;
	private final int size = 4096;
	   /**
	    * constructor for the buffer 
	    * @param index int index of the buffer
	    * @param filename the filename we are reading and writing to 
	    * @throws IOException exception thrown for the random access file
	    */
	public Buffer(int index, RandomAccessFile filename) throws IOException {
		dirtyBit = 0;
		val = new byte[size];
		blockIndex = index/size;
		file = filename;
		file.seek(blockIndex * size);
		file.read(val);
	}
	/**
	 * 
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public byte[] readBlock(int index) throws IOException {
		byte[] record = new byte[4];
		System.arraycopy(val, index % size, record, 0, record.length);
		return record;
	}

	
	/**
	 * method to write the buffer to the block 
	 * @param written byte array to be written 
	 * @param index index we are writting to
	 * @throws IOException exception thrown to handle errors 
	 */
	public void writeToBlock(byte[] written, int index) throws IOException {
		System.arraycopy(written, 0, val, index % size, written.length);
		dirtyBit = 1;
	}
	

	/**
	 * method to release the buffer using the dirty bit value
	 * @throws IOException for handling exceptions 
	 */
	public void releaseBuffer() throws IOException {
		if (dirtyBit == 1) {
			file.seek(blockIndex * size);
			file.write(val);
		}
	}

	/**
	 * getBlockIndex 
	 * @return the blockIndex integer
	 */
	public int getBlockIndex() {
		return blockIndex;
	}
}