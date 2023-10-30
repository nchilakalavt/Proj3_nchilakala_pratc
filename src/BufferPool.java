import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.io.IOException;

public class BufferPool {
	private Buffer[] pool;
	private RandomAccessFile filename;
	private int count;

	public BufferPool(int size, RandomAccessFile file) {
		pool = new Buffer[size];
		filename = file;
		count = 0;
	}

	public short acquireRecord(int index) throws IOException {
		return read(index).getRecord(index);
	}

	public Buffer read(int index) throws IOException {
		int poolIndex = findPoolIndex(index);
		if (poolIndex != -1) {
			Buffer temp = pool[poolIndex];
			for (int i = count - 1; i > 0; i--) {
				pool[i] = pool[i - 1];
			}
			pool[0] = temp;
			// 
			return temp;
		} else {
			Buffer bufferAdd = new Buffer(index);
			bufferAdd.readBlock(filename);
			return add(bufferAdd);
		}
	}

	public Buffer write(byte[] written, int index) throws IOException {
		int poolIndex = findPoolIndex(index);
		if (poolIndex != -1) {
			Buffer temp = pool[poolIndex];
			for (int i = count -1 ; i > 0; i--) {
				pool[i] = pool[i - 1];
			}
			temp.writeToBlock(written, index, filename);
			pool[0] = temp;
			return temp;
		} else {
			Buffer bufferAdd = new Buffer(index);
			bufferAdd.writeToBlock(written, bufferAdd.getIndex(), filename);
			return add(bufferAdd);
		}
	}

	private int findPoolIndex(int index) {
		if (count == 0) {
			return -1;
		} else {
			for (int i =  0; i < count; i++) {
				//error here cus count shouldn't be null
				if (pool[i] != null && pool[i].getBlockIndex() == index/4096) {
					return i;
				}
			}
			return -1;
		}
	}

	private Buffer add(Buffer buff) throws IOException {
		if (count == pool.length) {
			pool[pool.length - 1].releaseBuffer(filename);
			count--;
		}
		for (int i = count-1; i > 0; i--) {
			pool[i] = pool[i - 1];
		}
		pool[0] = buff;
		count++;
		return buff;
	}
	public void flushPool() throws IOException {
		for (int i = 0; i < count; i++) {
			pool[i].releaseBuffer(filename);
		}
	}
	public void swap(int indexOne, int indexTwo) throws IOException {
		byte[] buffOne = new byte[4];
		byte[] buffTwo = new byte[4];
		ByteBuffer wrapOne = ByteBuffer.wrap(read(indexOne).getVal());
		wrapOne.position(indexOne % 4096);
		wrapOne.get(buffOne);
		ByteBuffer wrapTwo = ByteBuffer.wrap(read(indexTwo).getVal());
		wrapTwo.position(indexTwo % 4096);
		wrapTwo.get(buffTwo);
		write(buffOne, indexTwo);
		write(buffTwo, indexOne);
	}
}
