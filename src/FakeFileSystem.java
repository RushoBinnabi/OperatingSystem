/**
 * Name: Rusho Binnabi
 * Date: 2/17/2024
 * Assignment: 3 - Devices
 * Class: ICSI 412 - Spring 2024
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class FakeFileSystem implements Device {

    // this FakeFileSystem class has various functions for working with files in a file system.

    private final RandomAccessFile[] randomAccessFiles = new RandomAccessFile[10];

    /**
     * empty FakeFileSystem() constructor.
     */

    public FakeFileSystem() {

    }

    /**
     * this getRandomAccessFiles() method gets the array of random access files.
     * @return the array of random access files.
     */

    public RandomAccessFile[] getRandomAccessFiles() {
        return randomAccessFiles;
    }


    /**
     * this open() method creates and records a RandomAccessFile in the array of random access files.
     * @param s a filename.
     * @return returns the file index.
     * @throws Exception if the string argument for the filename is empty or null.
     */

    @Override
    public int open(String s) throws Exception {
        int i;
        if (s == null || s.isEmpty()) {
            throw new Exception();
        }
        else {
            for (i = 0; i < getRandomAccessFiles().length; i++) {
                try (RandomAccessFile randomAccessFile = new RandomAccessFile(s, "r")) {
                    getRandomAccessFiles()[i] = randomAccessFile;
                }
                catch (Exception e) {
                    System.out.println("Error. The random access file could not be created and recorded in the array.");
                }
            }
        }
        return i;
    }

    /**
     * this close() method loops through the array of random access files and clears them.
     * it also closes the RandomAccessFile.
     * @param id the device id.
     */

    @Override
    public void close(int id) throws IOException {
        for (int i = 0; i < getRandomAccessFiles().length; i++) {
            getRandomAccessFiles()[id] = null;
        }
        for (RandomAccessFile randomAccessFile : getRandomAccessFiles()) {
            randomAccessFile.close();
        }
    }

    /**
     * this read() method creates and fills an array with random values that will be read from
     * the device associated with the device id.
     * @param id the device id.
     * @param size the size of the array?
     * @return an array of bytes that has the data that was read.
     */

    @Override
    public byte[] read(int id, int size) {
        byte[] array = new byte[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) id;
        }
        return array;
    }

    /**
     * this seek() method reads a number of bytes from the device associated with the device id.
     * @param id the device id.
     * @param to how much data to seek?
     */

    @Override
    public void seek(int id, int to) {
        System.out.println(Arrays.toString(read(id, to)));
    }

    /**
     * this write() method writes a number of bytes to the device associated with the device id.
     * @param id the device id.
     * @param data the array that has the bytes of data that will be written to the device
     * with the associated device id.
     * @return the number of bytes of data that was written.
     */

    @Override
    public int write(int id, byte[] data) {
        int i = 0;
        if (id < 0) {
            return -1;
        }
        else {
            while (i < data.length) {
                data[i] = read(id, data.length)[id];
                i++;
                if (i == data.length) {
                    return 0;
                }
            }
        }
        return i;
    }
}
