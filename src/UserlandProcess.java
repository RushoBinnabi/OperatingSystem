/**
 * Name: Rusho Binnabi
 * Date: 1/17/2024
 * Assignment: 1 - The Beginning
 * Class: ICSI 412 - Spring 2024
 */

import java.util.concurrent.Semaphore;

public abstract class UserlandProcess implements Runnable {

    // this class is the base class that will be used by every test program.

    /**
     * empty UserlandProcess() constructor.
     */
    public UserlandProcess() {

    }

    private static final int[][] tlb = new int[2][2];

    private static final byte[] physicalMemory = new byte[1048576];
    public Thread thread;

    public Semaphore semaphore = new Semaphore(1);

    private boolean expiredQuantum;

    private int priorityVariable;

    /**
     * this getPhysicalMemory() method gets the byte array that is the physical memory.
     * @return the byte array of physical memory.
     */

    public static byte[] getPhysicalMemory() {
        return physicalMemory;
    }

    /**
     * this getTlb() method gets the static multidimensional array of integers
     * which is the mapping between virtual address -> physical address.
     * @return the multidimensional array of integers.
     */
    public static int[][] getTlb() {
        return tlb;
    }

    /**
     * this getPriorityVariable() method gets the priority variable which will be
     * used to determine if a process should be demoted in priority or not.
     * @return the priority variable.
     */
    public int getPriorityVariable() {
        return priorityVariable;
    }

    /**
     * this incrementPriorityVariable() method increments the priority variable if a process times out 5 times.
     * @param priorityVariable the priority variable being incremented.
     */

    public void incrementPriorityVariable(int priorityVariable) {
        this.priorityVariable += priorityVariable;
    }

    /**
     * this setExpiredQuantum() method sets the boolean for indicating when the quantum has expired.
     * @param isExpired the boolean indicating that the quantum has expired.
     */

    public void setExpiredQuantum(boolean isExpired) {
        expiredQuantum = isExpired;
    }

    /**
     * this getExpiredQuantum() method gets the quantum.
     * @return true if the quantum is expired, false otherwise.
     */
    public boolean getExpiredQuantum() {
        return expiredQuantum;
    }

    /**
     * this requestStop() method sets the boolean that indicates that the quantum for the process has expired.
     * @param setBoolean the boolean for the quantum being set.
     */

    public void requestStop(boolean setBoolean) {
        setExpiredQuantum(setBoolean);
    }

    /**
     * this main() method is the main part of the program.
     */

    public abstract void main();

    /**
     * this isStopped() method is used to indicate if the semaphore is 0 or not.
     * @return true if the semaphore is 0, false otherwise.
     */
    public boolean isStopped() {
        return semaphore.tryAcquire();
    }

    /**
     * this isDone() method is used to see if the Java thread is alive or not.
     * @return true if the thread is not alive, false otherwise.
     */
    public boolean isDone() {
        return !thread.isAlive();
    }

    /**
     * this start() method releases (increments) the semaphore which allows the thread to run.
     */
    public void start() {
        semaphore.release();
    }

    /**
     * this stop() method gets (decreases) the semaphore which allows the thread to be stopped from running.
     */

    public void stop() {
        try {
            semaphore.acquire();
        }
        catch (Exception e) {
            System.out.println("Error. There was a problem acquiring (decreasing) the semaphore.");
        }
    }

    /**
     * this run() method gets the semaphore and then calls the main() method.
     */

    public void run() {
        try {
            semaphore.acquire();
            main();
        }
        catch (Exception e){
            System.out.println("Error. There was a problem running the program.");
        }
    }

    /**
     * this cooperate() method checks to see if the boolean is true and if it is then it sets it to false
     * and calls the switchProcess() method from  the static OS class.
     */

    public void cooperate() {
        if (getExpiredQuantum()) {
            setExpiredQuantum(false);
            incrementPriorityVariable(1);
            OS.getScheduler().switchProcess();
        }
    }

    /**
     * this read() method simulates accessing and reading from memory.
     * @param address the address or the page size.
     * @return the physical address.
     */

    public byte read(int address) {
        int virtualPage;
        int pageOffset;
        int physicalPage;
        int physicalAddress = 0;
        for (int i = 0; i < getTlb().length; i++) {
            virtualPage = address / 1024;
            OS.getMapping(virtualPage); // checks the TLB to see if the virtual page -> physical page mapping is there for the page size.
            pageOffset = address % 1024;
            physicalPage = getTlb()[virtualPage][pageOffset];
            physicalAddress = physicalPage * 1024 + pageOffset;
        }
        return getPhysicalMemory()[physicalAddress];
    }

    /**
     * this write() method simulates accessing and writing to memory.
     * @param address the address or the page size.
     * @param value the value gets set to the physical address.
     */

    public void write(int address, byte value) {
        int virtualPage;
        int pageOffset;
        int physicalPage;
        int physicalAddress = 0;
        for (int i = 0; i < getTlb().length; i++) {
            virtualPage = address / 1024;
            OS.getMapping(virtualPage); // checks the TLB to see if the virtual page -> physical page mapping is there for the page size.
            pageOffset = address % 1024;
            physicalPage = getTlb()[virtualPage][pageOffset];
            physicalAddress = physicalPage * 1024 + pageOffset;
        }
        getPhysicalMemory()[physicalAddress] = value;
    }
}