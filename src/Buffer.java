
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Buffer {
	private byte[] val = new byte[4096];
	private byte dirtyBit;
	private int fileIndex;
	private int blockIndex;
	private ByteBuffer buff;
	private RandomAccessFile file;

	public Buffer(int index, RandomAccessFile filename) throws IOException {
		dirtyBit = 0;
		fileIndex = index;
		blockIndex = index/4096;
		file = filename;
		file.seek(index);
		file.read(val);
		buff = ByteBuffer.wrap(val);
	}

	public void writeToBlock(short written, int index) throws IOException {
		buff.putShort(index % 4096, written);
		dirtyBit = 1;
	}
	
	public short readToBlock(int index) {
		return buff.getShort(index % 4096);
	}


	public byte[] getVal() {
		return val;
	}


	public void releaseBuffer() throws IOException {
		if (dirtyBit == 1) {
			file.seek(fileIndex);
			file.write(val);
		}
	}

	public int getIndex() {
		return fileIndex;
	}
	
	public int getBlockIndex() {
		return blockIndex;
	}
}
