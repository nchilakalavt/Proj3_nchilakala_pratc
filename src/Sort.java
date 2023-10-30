import java.io.IOException;
import java.nio.ByteBuffer;

public class Sort {
	private BufferPool pool;

	public Sort(BufferPool inputPool) {
		pool = inputPool;
	}

	public void quicksort(int i, int j) throws IOException { // Quicksort
		int pivotIndex = findpivot(i, j); // Pick a pivot
		short pivotVal = pool.read(pivotIndex*4);
		pool.swap(pivotIndex*4, j*4); // Stick pivot at end
		// k will be the first position in the right subarray
		int k = partition(i, j - 1, pivotVal);
		pool.swap(k*4, j*4); // Put pivot in place
		if ((k - i) > 1) {
			quicksort(i, k - 1);
		} // Sort left partition
		if ((j - k) > 1) {
			quicksort(k + 1, j);
		} // Sort right partition
	}

	public int findpivot(int i, int j) {
		return (i + j) / 2;
	}

	public int partition(int left, int right, short pivot) throws IOException {
		while (left <= right) { // Move bounds inward until they meet

			short leftByteShort = pool.read(left*4);
			while (leftByteShort < pivot) {
				left+=1;
				leftByteShort = pool.read(left*4);

			}
			short rightByteShort = pool.read(right*4);
			while ((right >= left) && (rightByteShort >= pivot)) {
				right -= 1;
				if (right >= 0) {
					rightByteShort = pool.read(right*4);
				}

			}
			if (right > left) {
				pool.swap(left*4, right*4);
			} // Swap out-of-place values
		}
		return left; // Return first position in right partition
	}
}
