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
	// sorting method
//	private void quicksortHelp(int i, int j) throws IOException { // Quicksort
//		int pivotIndex = findpivot(i, j); // Pick a pivot
//		byte[] pivotValRecord = pool.read(pivotIndex*4);
//		
//		pool.swap(pivotValRecord, pool.read(j*4), pivotIndex*4, j*4); // Stick pivot at end
//		// k will be the first position in the right subarray
//		int k = partition(i, j - 1, getShort(pivotValRecord));
//		pool.swap(pool.read(k*4), pool.read(j*4), k*4, j*4); // Put pivot in place
//		if ((k - i) > 1) {
//			quicksortHelp(i, k - 1);
//		} // Sort left partition
//		if ((j*4 - k*4) > 1) {
//			quicksortHelp(k + 1, j);
//		} // Sort right partition
//		
//	}

	private void quicksortHelp(int i, int j) throws IOException {
		if (i < j) {
			int pivot = partition(i, j, getShort(pool.read(j*4)));
			quicksortHelp(i, pivot - 1);
			quicksortHelp(pivot + 1, j);
		}
	}

	public int findpivot(int i, int j) {
		return (i + j) / 2;
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
//			byte[] leftTest = pool.read(i*4);
//			if (!Arrays.equals(leftRecord, leftTest)) {
//				System.out.println("i:" + i * 4 + "\n");
//			}
//			byte[] rightTest = pool.read(j*4);
//			if (!Arrays.equals(rightRecord, rightTest)) {
//				System.out.println("j:" + j * 4 + "\n");
//			}
			pool.swap(leftRecord, rightRecord, i * 4, j * 4);
		}
		pool.swap(pool.read(i * 4), pool.read(right * 4), i * 4, right * 4);
		return i; // Return first position in right partition
	}

	private short getShort(byte[] record) {
		return ByteBuffer.wrap(record).getShort();
	}
}
