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
	//checks if the index is at the top of the pool or if it is in the pool. if not it will create a new block and add it to the pool and return the record at the given index
	public byte[] read(int index) throws IOException {
		if (pool[0] != null && index/4096 == pool[0].getBlockIndex()) {
			return pool[0].readBlock(index);
		}
		int poolIndex = findPoolIndex(index);
		if (poolIndex != -1) {
			Buffer temp = pool[poolIndex];
			System.arraycopy(pool, 0, pool, 1, poolIndex);
			pool[0] = temp;
			return pool[0].readBlock(index);
		} else {
			Buffer bufferAdd = new Buffer(index, filename);
			add(bufferAdd);
			return bufferAdd.readBlock(index);
		}
	}
	//writes the record to a certain index and checks lru
	public void write(byte[] written, int index) throws IOException {
		if (pool[0] != null && index/4096 == pool[0].getBlockIndex()) {
			pool[0].writeToBlock(written, index);
			return;
		}
		int poolIndex = findPoolIndex(index);
		Buffer temp = pool[poolIndex];
		System.arraycopy(pool, 0, pool, 1, poolIndex);
		temp.writeToBlock(written, index);
		pool[0] = temp;
	}
	//finds where in the pool the index is. returns -1 if not in the pool
	private int findPoolIndex(int index) {
		if (count == 0) {
			return -1;
		} else {
			for (int i = 0; i < count; i++) {
				if (pool[i].getBlockIndex() == index / 4096) {
					return i;
				}
			}
			return -1;
		}
	}
	//adds the buffer to the pool and shifts the rest of the elements down by one
	private Buffer add(Buffer buff) throws IOException {
		if (count == pool.length) {
			pool[pool.length - 1].releaseBuffer();
			count -= 1;
		}
		System.arraycopy(pool, 0, pool, 1, pool.length-1);
		pool[0] = buff;
		count += 1;
		return buff;
	}
	//writes all the remaining buffers in the pool to the file
	public void flushPool() throws IOException {
		for (int i = 0; i < count; i++) {
			pool[i].releaseBuffer();
		}
	}
	//writes the record at index one to index two and vice versa
	public void swap(byte[] recordOne, byte[] recordTwo, int indexOne, int indexTwo) throws IOException {
		write(recordOne, indexTwo);
		write(recordTwo, indexOne);
	}
}
