import java.io.IOException;
import java.io.RandomAccessFile;
import student.TestCase;

/**
 * @author Nirish Chilakala (nchilakala) & Pratham Chopra (pratc)
 * @version 10/17/23
 */
public class QuicksortTest extends TestCase {
    private CheckFile fileChecker;

    /**
     * Sets up the tests that follow. In general, used for initialization.
     */
    public void setUp() {
        fileChecker = new CheckFile();
    }

    /**
     * This method is a demonstration of the file generator and file checker
     * functionality. It calles generateFile to create a small "ascii" file.
     * It then calls the file checker to see if it is sorted (presumably not
     * since we don't call a sort method in this test, so we assertFalse).
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testFileGenerator()
        throws Exception
    {
        String[] args = new String[3];
        Quicksort.generateFile("input.txt", "1000", 'a');
        args[0] = "input.txt";
        args[1] = "10";
        args[2] = "statFile.txt";
        Quicksort.main(args);
        assertTrue(fileChecker.checkFile("input.txt"));
    }
    
//    public void testFileSort() throws IOException {
//        String[] args = new String[3];
//        Quicksort.generateFile("input.txt", "1", 'a');
//        args[0] = "input.txt";
//        args[1] = "10";
//        args[2] = "statFile.txt";
//        BufferPool buffer = new BufferPool(10, new RandomAccessFile("input.txt", "rw"));
//        Sort sorter = new Sort(buffer);
//        buffer.swap(0, 8000);
//    }

    /**
     * Get code coverage of the class declaration.
     */
    public void testQInit() {
        Quicksort tree = new Quicksort();
        assertNotNull(tree);
        //Quicksort.main(null);
    }
}
