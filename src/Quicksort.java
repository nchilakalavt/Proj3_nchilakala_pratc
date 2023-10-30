
/**
 * Project 3 Computer Science 3114
 */

import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * The class containing the main method.
 *
 * @author Nirish Chilakala (nchilakala) & Pratham Chopra (pratc)
 * @version 10/16/23
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Quicksort {

	/**
	 * This method is used to generate a file of a certain size, containing a
	 * specified number of records.
	 *
	 * @param filename  the name of the file to create/write to
	 * @param blockSize the size of the file to generate
	 * @param format    the format of file to create
	 * @throws IOException throw if the file is not open and proper
	 */
	public static void generateFile(String filename, String blockSize, char format) throws IOException {
		FileGenerator generator = new FileGenerator();
		String[] inputs = new String[3];
		inputs[0] = "-" + format;
		inputs[1] = filename;
		inputs[2] = blockSize;
		generator.generateFile(inputs);
	}

	/**
	 * Main method
	 * 
	 * @param args Command line parameters.
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		RandomAccessFile file = new RandomAccessFile(args[0], "rw");
		BufferPool buff = new BufferPool(Integer.parseInt(args[1]), file);

		Sort sorter = new Sort(buff);
		sorter.quicksort(0, (int)((file.length()/4) - 1));
		buff.flushPool();
		file.close();
	}
}
