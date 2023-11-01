import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Sort {
	private BufferPool pool;

	public Sort(BufferPool inputPool) {
		pool = inputPool;
	}

	// runs quicksort help and flushes pool once its done
	public void quicksort(int i, int j) throws IOException {
		quicksortHelp(i, j);
		pool.flushPool();
	}

	private void quicksortHelp(int i, int j) throws IOException {
		if (i < j) {
			int pivot = partition(i, j, getShort(pool.read(j * 4)));
			quicksortHelp(i, pivot - 1);
			quicksortHelp(pivot + 1, j);
		}
	}


	public int partition(int left, int right, short pivot) throws IOException {
		int i = left - 1;
		int j = right;
		while (true) { // Move bounds inward until they meet
			byte[] leftRecord = pool.read((++i) * 4);
			short leftByteShort = getShort(leftRecord);
			while (leftByteShort < pivot) {
				leftRecord = pool.read((++i) * 4);
				leftByteShort = getShort(leftRecord);

			}
			byte[] rightRecord = pool.read((--j) * 4);
			short rightByteShort = getShort(rightRecord);
			while ((j > i) && (rightByteShort > pivot)) {
				rightRecord = pool.read((--j) * 4);
				rightByteShort = getShort(rightRecord);
			}
			if (j <= i) {
				break;
			} // Swap out-of-place values
			pool.swap(leftRecord, rightRecord, i * 4, j * 4);
		}
		pool.swap(pool.read(i * 4), pool.read(right * 4), i * 4, right * 4);
		return i; // Return first position in right partition
	}

	private short getShort(byte[] record) {
		return ByteBuffer.wrap(record).getShort();
	}
}
