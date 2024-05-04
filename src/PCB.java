import java.util.LinkedList;
import java.util.Queue;

/**
 * Name: Rusho Binnabi
 * Date: 1/27/2024
 * Assignment: 2 - Priority Scheduler
 * Class: ICSI 412 - Spring 2024
 */
public class PCB {

    // this class manages the process from the Kernel's perspective and is not visible from UserlandProcess.

    private final VirtualToPhysicalMapping[] memoryMap = new VirtualToPhysicalMapping[100];
    private static int nextPiD;
    private int processIDPID;
    private UserlandProcess userlandProcess;

    private final int[] integerArray = new int[10];

    private String name = "";

    private static final Queue<KernelMessage> messageQueue = new LinkedList<>();

    private Kernel kernel;

    /**
     * this getKernel() method gets the kernel.
     * @return the kernel.
     */
    public Kernel getKernel() {
        return kernel;
    }

    /**
     * this getMapping() method gets the array of VirtualToPhysicalMapping that maps the data.
     * @return the array of VirtualToPhysicalMapping that maps the data.
     */
    public VirtualToPhysicalMapping[] getMemoryMap() {
        return memoryMap;
    }

    /**
     * this getMessageQueue() gets the message queue.
     * @return the message queue.
     */

    public Queue<KernelMessage> getMessageQueue() {
        return messageQueue;
    }

    /**
     * this getName() method gets the name of the UserlandProcess.
     * @return the name of the UserlandProcess.
     */

    public String getName() {
        return name.getClass().getSimpleName();
    }

    /**
     * this setName() method sets the name of the UserlandProcess.
     * @param name the name of the UserlandProcess being set.
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * this getIntegerArray() method gets the array of integers that has the process data for the devices.
     * @return the array of integers that has the process data for the devices.
     */

    public int[] getIntegerArray() {
        return integerArray;
    }

    /**
     * this PCB() constructor creates the thread and sets the pid.
     * @param up the userland process.
     */

    public PCB(UserlandProcess up) {
        setUserlandProcess(up);
        setNextPiD(0);
    }

    /**
     * this setNextPid() method sets the next pid.
     * @param nextPiD the next pid being set.
     */

    public void setNextPiD(int nextPiD) {
        PCB.nextPiD = nextPiD;
    }

    /**
     * this getNextPid() method gets the next pid.
     * @return the next pid.
     */
    public int getNextPiD() {
        return nextPiD;
    }

    /**
     * this setProcessIDPID() method sets the pid for the process id.
     * @param processIDPID the pid for the process id being set.
     */
    public void setProcessIDPID(int processIDPID) {
        this.processIDPID = processIDPID;
    }

    /**
     * this getProcessIDPID() method gets the pid for the process id.
     * @return the pid for the process id.
     */
    public int getProcessIDPID() {
        return processIDPID;
    }

    /**
     * this getUserlandProcess() method gets the userland process.
     * @return the userland process.
     */
    public UserlandProcess getUserlandProcess() {
        return userlandProcess;
    }

    /**
     * this setUserlandProcess() method sets the userland process.
     * @param up the userland process being set.
     */
    public void setUserlandProcess(UserlandProcess up) {
        userlandProcess = up;
    }

    /**
     * this stop() method calls the stop() method from the userland process.
     * It also loops with the Thread.sleep() method until the boolean return value
     * of the isStopped() method from the userland process is true.
     */
    public void stop() {
        if (getUserlandProcess().isStopped()) {
            while (getUserlandProcess().isStopped()) {
                try {
                    Thread.sleep(250);
                    getUserlandProcess().stop();
                }
                catch (Exception e) {
                    System.out.println("Error. The process could not be stopped.");
                }
            }
        }
    }

    /**
     * this isDone() method calls the isDone() method from the Userland Process.
     * @return true if the return value from the isDone() method from the Userland
     * Process is true, false otherwise.
     */

    public boolean isDone() {
        return getUserlandProcess().isDone();
    }

    /**
     * this run() method calls the start() method from the Userland Process.
     */
    public void run() {
        getUserlandProcess().start();
    }
}
