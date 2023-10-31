import java.io.IOException;
import java.nio.ByteBuffer;

public class Sort {
	private BufferPool pool;

	public Sort(BufferPool inputPool) {
		pool = inputPool;
	}
	
	//runs quicksort help and flushes pool once its done
	public void quicksort(int i, int j) throws IOException {
		quicksortHelp(i, j);
		pool.flushPool();
	}
	//sorting method
	private void quicksortHelp(int i, int j) throws IOException { // Quicksort
		int pivotIndex = findpivot(i, j); // Pick a pivot
		byte[] pivotValRecord = pool.read(pivotIndex*4);
		
		pool.swap(pivotValRecord, pool.read(j*4), pivotIndex*4, j*4); // Stick pivot at end
		// k will be the first position in the right subarray
		int k = partition(i, j - 1, getShort(pivotValRecord));
		pool.swap(pool.read(k*4), pool.read(j*4), k*4, j*4); // Put pivot in place
		if ((k - i) > 1) {
			quicksortHelp(i, k - 1);
		} // Sort left partition
		if ((j*4 - k*4) > 1) {
			quicksortHelp(k + 1, j);
		} // Sort right partition
		
	}

	public int findpivot(int i, int j) {
		return (i + j) / 2;
	}

	public int partition(int left, int right, short pivot) throws IOException {
		while (left <= right) { // Move bounds inward until they meet
			byte[] leftRecord = pool.read(left*4);
			short leftByteShort = getShort(leftRecord);
			while (leftByteShort < pivot) {
				left+=1;
				leftRecord = pool.read(left*4);
				leftByteShort = getShort(leftRecord);

			}
			byte[] rightRecord = pool.read(right*4);
			short rightByteShort = getShort(rightRecord);
			while ((right >= left) && (rightByteShort >= pivot)) {
				right -= 1;
				if (right >= 0) {
					rightRecord = pool.read(right*4);
					rightByteShort = getShort(rightRecord);
				}

			}
			if (right > left) {
				pool.swap(leftRecord, rightRecord, left*4, right*4);
			} // Swap out-of-place values
		}
		return left; // Return first position in right partition
	}
	
	private short getShort(byte[] record) {
		return ByteBuffer.wrap(record).getShort();
	}
}
