
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Buffer {
	private byte[] val;
	private byte dirtyBit;
	private int fileIndex;
	private int blockIndex;

	public Buffer(int index) {
		dirtyBit = 0;
		val = new byte[4096];
		fileIndex = index;
		blockIndex = index/4096;
	}

	public byte[] readBlock(RandomAccessFile filename) throws IOException {
		filename.seek(fileIndex);
		filename.read(val);
		byte[] retVal = val;
		return retVal;

	}

	public void writeToBlock(byte[] written, int index, RandomAccessFile filename) throws IOException {
		int buffIndex = index % 4096;
		for (int i = 0; i < written.length; i++) {
			val[buffIndex + i] = written[i];
		}
		dirtyBit = 1;
	}
	
	public short getRecord(int index) {
		ByteBuffer buff = ByteBuffer.wrap(val);
		return buff.getShort(index % 4096);
	}


	public byte[] getVal() {
		return val;
	}


	public void releaseBuffer(RandomAccessFile filename) throws IOException {
		if (dirtyBit == 1) {
			filename.seek(fileIndex);
			filename.write(val);
		}
	}

	public int getIndex() {
		return fileIndex;
	}
	
	public int getBlockIndex() {
		return blockIndex;
	}
}
