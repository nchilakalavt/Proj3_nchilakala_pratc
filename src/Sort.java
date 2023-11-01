import java.io.IOException;
import java.nio.ByteBuffer;


public class Sort {
	private BufferPool pool;
	private final int byteConvert = 4;

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
			int pivot = partition(i, j, getShort(pool.read(j * byteConvert)));
			quicksortHelp(i, pivot - 1);
			quicksortHelp(pivot + 1, j);
		}
	}


	public int partition(int left, int right, short pivot) throws IOException {
		int i = left - 1;
		int j = right;
		while (true) { // Move bounds inward until they meet
			byte[] leftRecord = pool.read((++i) * byteConvert);
			short leftByteShort = getShort(leftRecord);
			while (leftByteShort < pivot) {
				leftRecord = pool.read((++i) * byteConvert);
				leftByteShort = getShort(leftRecord);

			}
			byte[] rightRecord = pool.read((--j) * byteConvert);
			short rightByteShort = getShort(rightRecord);
			while ((j > i) && (rightByteShort > pivot)) {
				rightRecord = pool.read((--j) * byteConvert);
				rightByteShort = getShort(rightRecord);
			}
			if (j <= i) {
				break;
			} // Swap out-of-place values
			pool.swap(leftRecord, rightRecord, i * byteConvert, j * byteConvert);
		}
		pool.swap(pool.read(i * byteConvert), pool.read(right * byteConvert), i * byteConvert, right * byteConvert);
		return i; // Return first position in right partition
	}

	private short getShort(byte[] record) {
		return ByteBuffer.wrap(record).getShort();
	}
}
