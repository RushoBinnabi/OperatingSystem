/**
 * Name: Rusho Binnabi
 * Date: 1/17/2024
 * Assignment: 1 - The Beginning
 * Class: ICSI 412 - Spring 2024
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Kernel implements Device, Runnable {

    // this class is the kernel and will have some similarities to the UserlandProcess class.

    private static final Kernel kernel;
    private Scheduler scheduler;

    private Thread thread;

    private Semaphore semaphore;

    private final VirtualFileSystem vfs = new VirtualFileSystem();

    private final int[] integerArray = new int[10];

    private PCB pcb;

    private final HashMap<Integer, PCB> kernelandProcesses = new HashMap<>();

    private KernelMessage kernelMessage;

    private final HashMap<Integer, PCB> waitingProcesses = new HashMap<>();

    private final boolean[] freeSpace = new boolean[1000];

    private FakeFileSystem fakeFileSystem;

    private int pageNumber;

    private int fileDescriptor;

    /**
     * this getFileDescriptor() method gets the file descriptor.
     * @return the file descriptor.
     */

    public int getFileDescriptor() {
        return fileDescriptor;
    }

    /**
     * this setFileDescriptor() method sets the file descriptor.
     * @param fileDescriptor the file descriptor being set.
     */

    public void setFileDescriptor(int fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    /**
     * this getPageNumber() method gets the page number.
     * @return the page number.
     */

    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * this setPageNumber() method sets the page number.
     * @param pageNumber the page number being set.
     */

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * this getFakeFileSystem() method gets the fake file system.
     * @return the fake file system.
     */
    public FakeFileSystem getFakeFileSystem() {
        return fakeFileSystem;
    }

    /**
     * this getFreeSpace() method gets the array of booleans that indicates if a page is in use or not.
     * @return the array of booleans that indicates if a page is in use or not.
     */

    public boolean[] getFreeSpace() {
        return freeSpace;
    }

    /**
     * this getWaitingProcesses() method gets the hashmap of waiting processes.
     * @return the hashmap of waiting processes.
     */

    public HashMap<Integer, PCB> getWaitingProcesses() {
        return waitingProcesses;
    }

    /**
     * this getKernelMessage() method gets the KernelMessage object.
     * @return the KernelMessage object.
     */

    public KernelMessage getKernelMessage() {
        return kernelMessage;
    }

    /**
     * this getKernelandProcesses() gets the hashmap of kerneland processes.
     * @return the hashmap of kerneland processes.
     */
    public HashMap<Integer, PCB> getKerlandProcesses() {
        return kernelandProcesses;
    }

    /**
     * this getPcb() method gets the PCB.
     * @return the PCB.
     */

    public PCB getPcb() {
        return pcb;
    }

    /**
     * this setPcb() method sets the PCB.
     * @param up the userland process being used to set the PCB.
     */

    public void setPcb(UserlandProcess up) {
        pcb = new PCB(up);
    }

    /**
     * this getVfs() method gets the virtual file system.
     * @return the virtual file system.
     */
    public VirtualFileSystem getVfs() {
        return vfs;
    }

    /**
     * this Kernel() constructor initializes the objects and variables.
     */
    private Kernel() {
        setScheduler(new Scheduler());
        setThread(new Thread());
        setSemaphore(new Semaphore(1));
        thread.start();
        setPcb(new UserlandProcess() {
            @Override
            public void main() {

            }
        });
    }

    // this static block makes it so that the one and only instance of the Kernel class can be made.

    static {
        kernel = new Kernel();
    }

    /**
     * this getKernelInstance() method gets the one and only instance of the Kernel class.
     * @return the one and only instance of the Kernel class.
     */

    public static Kernel getKernelInstance() {
        return kernel;
    }

    /**
     * this setScheduler() method sets the scheduler.
     * @param scheduler the scheduler being set.
     */

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * this getScheduler() method gets the scheduler.
     * @return the scheduler.
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * this getThread() method gets the thread.
     * @return the thread.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * this setThread() method sets the thread.
     * @param thread the thread being set.
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * this getSemaphore() method gets the semaphore.
     * @return the semaphore.
     */
    public Semaphore getSemaphore() {
        return semaphore;
    }

    /**
     * this setSemaphore() method sets the semaphore.
     * @param semaphore the semaphore being set.
     */
    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    /**
     * this start() method releases (increments) the semaphore which stops the thread from running.
     */
    public void start() {
        getSemaphore().release();
        try {
            setFileDescriptor(getFakeFileSystem().open("pagefile.txt"));
        }
        catch (Exception e) {
            System.out.println("Error. The swap file couldn't be created.");
        }
    }

    /**
     * this run() method is an infinite loop that checks the semaphore to see what should be run, call the function
     * that implements each of the current calls of CreateProcess and SwitchProcess, and then calls the run() method
     * on the next process to run.
     */
    public void run() {
        try {
            while (true) {
                getSemaphore().acquire();
                switch (OS.getCurrentCall()) {
                    case CreateProcess:
                        getScheduler().createProcess(getScheduler().getCurrentlyRunning().getUserlandProcess());
                        break;
                    case SwitchProcess:
                        if (getScheduler().getCurrentlyRunning().getUserlandProcess().getPriorityVariable() == 5) {
                            getScheduler().demote();
                        }
                        getScheduler().switchProcess();
                        break;
                    case PriorityProcess:
                        getScheduler().createProcess(getScheduler().getCurrentlyRunning().getUserlandProcess(), OS.getCurrentCall());
                        break;
                    case SleepProcess:
                        getScheduler().createProcess(getScheduler().getCurrentlyRunning().getUserlandProcess(), OS.getCurrentCall());
                        getScheduler().sleep(250);
                        break;
                    default:
                        createProcessNew(getScheduler().getCurrentlyRunning().getUserlandProcess(), OS.CallType.PriorityProcess);
                        break;
                }
                getScheduler().getCurrentlyRunning().run();
            }
        }
        catch (Exception e) {
            System.out.println("Error. The process could not be run.");
        }
    }

    /**
     * this new version of the createProcess() method does pretty much the same thing as the old version
     * except this version uses both UserlandProcess and a new enum for Priority for the Kernel.
     * @param up the userland process.
     * @param callType the priority enum.
     */

    public int createProcessNew(UserlandProcess up, OS.CallType callType) {
        OS.setFunctionParameters(up);
        OS.getFunctionParameters().remove(up);
        OS.setCurrentCall(callType);
        getKernelInstance().start();
        if (getScheduler().getCurrentlyRunning() != null) {
            OS.setReturnValue(getScheduler().getCurrentlyRunning());
            OS.getKernel().getScheduler().getCurrentlyRunning().stop();
        }
        else {
            try {
                while (true) {
                    Thread.sleep(10);
                }
            }
            catch (Exception e) {
                System.out.println("Error. There was nothing running.");
            }
        }
        return (int) OS.getReturnValue();
    }

    /**
     * this sleep() method creates the Enum for the sleep, puts the parameters in the list,
     * switches to the kernel in the scheduler, gets the return value, etc., when processes get put to sleep.
     * @param milliseconds the time for processes to sleep in milliseconds.
     */

    public void sleep(int milliseconds) {
        OS.setFunctionParameters(milliseconds);
        OS.getFunctionParameters().remove(getScheduler().getCurrentlyRunning());
        OS.setCurrentCall(OS.CallType.valueOf("SleepProcess"));
        OS.getKernel().start();
        if (OS.getScheduler().getCurrentlyRunning() != null) {
            OS.setReturnValue(OS.getScheduler().getCurrentlyRunning());
            OS.getKernel().getScheduler().getCurrentlyRunning().stop();
        }
        else {
            try {
                while (true) {
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                System.out.println("Error. There was nothing sleeping.");
            }
            System.out.println(OS.getReturnValue());
        }
    }

    /**
     * this open() method gets a device id from the virtual file system and puts it into the array of userland processes.
     * @param s the filename.
     * @return the array index from the array of userland processes.
     */

    @Override
    public int open(String s) throws Exception {
        int i;
        for (i = 0; i < getScheduler().getCurrentlyRunning().getIntegerArray().length; i++) {
            if (getScheduler().getCurrentlyRunning().getIntegerArray()[i] == 0) {
                return -1;
            }
            else if (getVfs().open(s) == -1) {
                return -1;
            }
            else {
                getPcb().getIntegerArray()[i] = getVfs().open(s);
            }
        }
        return getPcb().getIntegerArray()[i];
    }

    /**
     * this close() method does the same thing as the open() method does except it also sets the array entry to -1.
     * @param id the device id.
     * @throws IOException throws an IOException if something goes wrong during the processing.
     */

    @Override
    public void close(int id) throws IOException {
        int idNumber;
        for (int i = 0; i < getPcb().getIntegerArray().length; i++) {
            idNumber = getPcb().getIntegerArray()[i];
            getVfs().close(idNumber);
            getPcb().getIntegerArray()[i] = -1;
        }
        if (getScheduler().getCurrentlyRunning().isDone()) {
            for (Device device : getVfs().getDeviceArray()) {
                device.close(id);
            }
        }
    }

    /**
     * this read() method gets an id from the userland process, uses the array in the userland process to
     * convert it to the id for the virtual file system and use that id for the virtual file system.
     * @param id the device id.
     * @param size the size of the array?
     * @return an array of bytes that was read.
     */

    @Override
    public byte[] read(int id, int size) {
        byte[] array = new byte[size];
        int idNumber = 0;
        for (int i = 0; i < array.length; i++) {
            idNumber = getPcb().getIntegerArray()[i];
        }
        return getVfs().read(idNumber, size); // passes the call through to the virtual file system's read() method.
    }

    /**
     * this seek() method does the same thing that the read() method does except it tries to seek a
     * number of bytes from what is being read when the call is passed through to the virtual file system.
     * @param id the device id.
     * @param to how much data to seek?
     */
    @Override
    public void seek(int id, int to) {
        System.out.println(Arrays.toString(getVfs().read(id, to))); // passes the call through to the virtual file system's read() method.
    }

    /**
     * this seek() method does the same thing that the read() method does except it tries to write a
     * number of bytes from what is being read when the call is passed through to the virtual file system.
     * @param id the device id.
     * @param data the array that has the bytes of data that will be written to the device
     * with the associated device id.
     * @return the number of bytes of data that was being written.
     */

    @Override
    public int write(int id, byte[] data) {
        int i = 0;
        if (id < 0) {
            return -1;
        }
        else {
            while (i < data.length) {
                data[i] = (byte) getVfs().write(id, data); // passes the call through to the virtual file system's write() method.
                i++;
                if (i == data.length) {
                    return 0;
                }
            }
        }
        return i;
    }

    /**
     * this getPID() method gets the current process' pid.
     * @return the current process' pid.
     */

    public static int getPID() {
        return OS.getScheduler().getCurrentlyRunning().getProcessIDPID();
    }

    /**
     * this getPIDByName() method gets the pid of a process with that name.
     * @param processPID the name of the process.
     * @return the pid of a process with that name.
     */

    public int getPIDByName(String processPID) {
        getScheduler().getCurrentlyRunning().setName(processPID);
        if (getScheduler().getCurrentlyRunning().getName().equals(processPID)) {
            return getPID();
        }
        else {
            return 0;
        }
    }

    /**
     * this sendMessage() method uses the copy constructor of the KernelMessage class to make a copy of the original message.
     * It also sets the pid of the sender.
     * @param km the kernel message.
     */

    public void sendMessage(KernelMessage km) {
        for (int i = 0; i < getKerlandProcesses().size(); i++) {
            if (getKerlandProcesses().containsKey(i)) {
                getPcb().getMessageQueue().add(km);
                if (waitForMessage() != null) {
                    if (i == getPcb().getMessageQueue().remove().getWhatMessage()) { // keeps track, for each process, if it needs to take the first message from the queue when the process is run.
                        getPcb().getMessageQueue().remove();
                    }
                    else {
                        getPcb().getMessageQueue().remove();
                    }
                }
            }
        }
        new KernelMessage(km);
    }

    /**
     * this waitForMessage() method waits for a message to get sent to it before a process runs.
     * @return the kernel message.
     */

    public KernelMessage waitForMessage() {
        if (getKernelMessage().getWhatMessage() != 0) {
            return getPcb().getMessageQueue().remove();
        }
        else {
            getWaitingProcesses().put(getKernelMessage().getWhatMessage(), getPcb());
            return null;
        }
    }

    /**
     * this allocateMemory() method allocates memory.
     * @param size the amount of memory to allocate.
     * @return the start of the virtual address.
     */

    public VirtualToPhysicalMapping allocateMemory(int size) {
        int i;
        for (i = 0; i < getFreeSpace().length; i++) {
            if (getPcb().getMemoryMap()[i] != null) {
                getPcb().getMemoryMap()[i] = new VirtualToPhysicalMapping();
                getFreeSpace()[i] = true;
            }
        }
        return getPcb().getMemoryMap()[i];
    }

    /**
     * this freeMemory() method frees memory.
     * @param pointer the virtual address.
     * @param size the amount of memory to free.
     * @return true if the memory was freed, false otherwise.
     */

    public boolean freeMemory(int pointer, int size) {
        if (OS.getVirtualToPhysicalMapping().getPhysicalPageNumber() != 1) {
            for (int i = 0; i < getFreeSpace().length; i++) {
                if (i == pointer) {
                    getFreeSpace()[i] = false;
                }
            }
            Arrays.fill(getPcb().getMemoryMap(), null); // sets each of the array entries for each entry back to null.
        }
        return false;
    }

}