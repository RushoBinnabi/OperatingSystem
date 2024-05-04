/**
 * Name: Rusho Binnabi
 * Date: 2/16/2024
 * Assignment: 3 - Devices
 * Class: ICSI 412 - Spring 2024
 */

import java.util.Arrays;
import java.util.Random;

public class RandomDevice implements Device {

    // this RandomDevice class has various functions for working with devices.

    private final Random[] arrayOfRandomItems = new Random[10];
    private int randomClassSeed;

    /**
     * this getArrayOfItems() method gets the array of random items.
     * @return the array of random items.
     */

    public Random[] getArrayOfRandomItems() {
        return arrayOfRandomItems;
    }

    /**
     * this getRandomClassSeed() method gets the random class seed which is the seed
     * for the Random class if the provided string for the open() method is not null or empty.
     * @return the random class seed.
     */

    public int getRandomClassSeed() {
        return randomClassSeed;
    }

    /**
     * this setRandomClassSeed() method sets the random class seed.
     * @param randomClassSeed the random class seed being set.
     */

    public void setRandomClassSeed(int randomClassSeed) {
        this.randomClassSeed = randomClassSeed;
    }

    /**
     * this RandomDevice() constructor puts a random object in the
     * array of random items if it isn't empty or null.
     * @param random the random item being put into the array.
     */

    public RandomDevice(Random random) {
        for (int i = 0; i < getArrayOfRandomItems().length; i++) {
            if (getArrayOfRandomItems()[i] == null) {
                getArrayOfRandomItems()[i] = random;
            }
        }
    }

    /**
     * this open() method creates a new RandomDevice and puts it into an empty spot in the array.
     * If the string argument for the method is not null or empty, then the string gets converted
     * into an integer to be used as the seed for the Random class.
     * @param s the string argument which could get converted into an integer if the string isn't null or empty.
     * @return a new RandomDevice that puts the random device into an empty spot in the array of random items
     * and returns a 0.
     */

    @Override
    public int open(String s) {
        if (s != null) {
            setRandomClassSeed(Integer.parseInt(s));
            return getRandomClassSeed();
        }
        else {
            return new RandomDevice(new Random()).getRandomClassSeed();
        }
    }

    /**
     * this close() method makes the random device entry in the array of random items null.
     * @param id the random device entry.
     */

    @Override
    public void close(int id) {
        getArrayOfRandomItems()[id] = null;
    }

    /**
     * this read() method creates or fills an array with random values.
     * @param id the device id.
     * @param size the size of the array.
     * @return the array.
     */

    @Override
    public byte[] read(int id, int size) {
        byte[] randomArrayValues = new byte[size];
        for (int i = 0; i < randomArrayValues.length; i++) {
            randomArrayValues[i] = (byte) id;
        }
        return randomArrayValues;
    }

    /**
     * this seek() method reads random bytes but doesn't return them.
     * @param id the device id.
     * @param to how much data to seek?
     */

    @Override
    public void seek(int id, int to) {
        System.out.println(Arrays.toString(read(id, to)));
    }

    /**
     * this write() method return a length of 0 and does nothing.
     * @param id the device id.
     * @param data the data being written.
     * @return 0.
     */

    @Override
    public int write(int id, byte[] data) {
        return 0;
    }
}
