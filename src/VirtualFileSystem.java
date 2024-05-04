/**
 * Name: Rusho Binnabi
 * Date: 2/18/2024
 * Assignment: 3 - Devices
 * Class: ICSI 412 - Spring 2024
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class VirtualFileSystem implements Device {

    // this class is just another device, but it maps calls to other devices and ids.

    private final Device[] deviceArray = new Device[10];
    private final int[] intArray = new int[10];

    /**
     * empty VirtualFileSystem() constructor.
     */

    public VirtualFileSystem() {

    }

    /**
     * this getDeviceArray() method gets the array of devices that maps a virtual file system id to a device and id combination.
     * @return the array of devices that maps a virtual file system id to a device and id combination.
     */

    public Device[] getDeviceArray() {
        return deviceArray;
    }

    /**
     * this getIntArray() method gets the array of integers that maps a virtual file system id to a device and id combination.
     * @return the array of integers that maps a virtual file system id to a device and id combination.
     */

    public int[] getIntArray() {
        return intArray;
    }

    /**
     * this open() method opens a random device if a number for the seed isn't provided
     * and if it is then it opens the random device and uses the number for the seed.
     * @param s the filename with or without a number for the seed.
     * @return the seed number when opening a random device.
     */

    @Override
    public int open(String s) {
        Scanner scanner = new Scanner(s);
        Device device;
        int seedNumber = 0;
        while (scanner.hasNext()) {
            if (scanner.nextLine().equals(s)) { // if the provided filename doesn't have a number for the seed.
                device = new RandomDevice(new Random());
                for (int i = 0; i < getDeviceArray().length; i++) {
                    getDeviceArray()[i] = device;
                }
            }
            else if (scanner.hasNextInt()) { // if the provided filename does have a number for the seed.
                seedNumber = scanner.nextInt();
                for (int i = 0; i < getIntArray().length; i++) {
                    getIntArray()[i] = seedNumber;
                }
            }
        }
        return seedNumber;
    }

    /**
     * this close() method removes the device and id entries.
     * @param id the device id.
     * @throws IOException throws an IOException if anything goes wrong during processing.
     */

    @Override
    public void close(int id) throws IOException {
        for (int i = 0; i < getIntArray().length; i++) {
            getIntArray()[id] = 0;
        }
        for (int i = 0; i < getDeviceArray().length; i++) {
            getDeviceArray()[id] = null;
        }
    }

    /**
     * this read() method creates and fills an array with values from a file device as it gets read.
     * @param id the device id.
     * @param size the size of the array?
     * @return an array of bytes that has the data that was read.
     */

    @Override
    public byte[] read(int id, int size) {
        byte[] array = new byte[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) getIntArray()[id];
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
