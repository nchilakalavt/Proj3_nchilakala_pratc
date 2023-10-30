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

	public short read(int index) throws IOException {
		int poolIndex = findPoolIndex(index);
		if (poolIndex != -1) {
			Buffer temp = pool[poolIndex];
			System.arraycopy(pool, 0, pool, 1, pool.length-1);
			pool[0] = temp;
			return temp.readToBlock(index);
		} else {
			Buffer bufferAdd = new Buffer(index, filename);
			add(bufferAdd);
			return bufferAdd.readToBlock(index);
		}
	}

	public void write(short written, int index) throws IOException {
		int poolIndex = findPoolIndex(index);
		if (poolIndex != -1) {
			Buffer temp = pool[poolIndex];
			System.arraycopy(pool, 0, pool, 1, pool.length-1);
			temp.writeToBlock(written, index);
			pool[0] = temp;
		} else {
			Buffer bufferAdd = new Buffer(index, filename);
			bufferAdd.writeToBlock(written, index);
			add(bufferAdd);
		}
	}

	private int findPoolIndex(int index) {
		if (count == 0) {
			return -1;
		} else {
			for (int i = 0; i < count; i++) {
				// error here cus count shouldn't be null
				if (pool[i].getBlockIndex() == index / 4096) {
					return i;
				}
			}
			return -1;
		}
	}

	private Buffer add(Buffer buff) throws IOException {
		if (count == pool.length) {
			pool[pool.length - 1].releaseBuffer();
			count-=1;
		}
		System.arraycopy(pool, 0, pool, 1, pool.length-1);
		pool[0] = buff;
		count+=1;
		return buff;
	}

	public void flushPool() throws IOException {
		for (int i = 0; i < count; i++) {
			pool[i].releaseBuffer();
		}
	}

	public void swap(int indexOne, int indexTwo) throws IOException {
		write(read(indexOne), indexTwo);
		write(read(indexTwo), indexOne);
	}
}
