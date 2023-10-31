
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Buffer {
	private byte[] val;
	private byte dirtyBit;
	private int fileIndex;
	private int blockIndex;
	private RandomAccessFile file;
	
	public Buffer(int index, RandomAccessFile filename) throws IOException {
		dirtyBit = 0;
		val = new byte[4096];
		fileIndex = index;
		blockIndex = index/4096;
		file = filename;
		file.seek(fileIndex);
		file.read(val);
	}
	//Finds the record at the index
	public byte[] readBlock(int index) throws IOException {
		byte[] record = new byte[4];
		System.arraycopy(val, index % 4096, record, 0, record.length);
		return record;
	}
	//writes the record to the index and sets dirty bit to 1 so we know we have to rewrite
	public void writeToBlock(byte[] written, int index) throws IOException {
		System.arraycopy(written, 0, val, index % 4096, written.length);
		dirtyBit = 1;
	}
	


	public byte[] getVal() {
		return val;
	}

	//rewrites the buffer in the file if the dirty bit is 1
	public void releaseBuffer() throws IOException {
		if (dirtyBit == 1) {
			file.seek(fileIndex);
			file.write(val);
		}
	}

	public int getIndex() {
		return fileIndex;
	}
	//finds which block the buffer is in (ex: 0, 1, 2, 3)
	public int getBlockIndex() {
		return blockIndex;
	}
}
