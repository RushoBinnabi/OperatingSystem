import java.util.Arrays;

/**
 * Name: Rusho Binnabi
 * Date: 1/17/2024
 * Assignment: 1 - The Beginning
 * Class: ICSI 412 - Spring 2024
 */

public class Main {

    // this class is the main() method of the program.

    private static final VirtualFileSystem vfs = new VirtualFileSystem();

    private static int deviceID1;

    private static final Scheduler scheduler = new Scheduler();

    /**
     * this getScheduler() method gets the scheduler.
     * @return the scheduler.
     */

    public static Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * this getDeviceID1() method gets the 1st device id.
     * @return the 1st device id.
     */

    public static int getDeviceID1() {
        return deviceID1;
    }

    /**
     * this setDeviceID1() sets the 1st device id.
     * @param deviceID the 1st device id being set.
     */

    public static void setDeviceID1(int deviceID) {
        deviceID1 = deviceID;
    }

    /**
     * this getVfs() method gets the virtual file system so that it can be used for testing devices.
     * @return the virtual file system.
     */

    public static VirtualFileSystem getVfs() {
        return vfs;
    }

    /**
     * this main() method calls the startup() method from the static OS class with a
     * new HelloWorld() object and then calls the createProcess() method using a new
     * GoodbyeWorld() object.
     * @param args the argument is the program being run.
     */

    public static void main(String[] args) {
        OS.createProcess(new Read());
        OS.getScheduler().switchProcess(); // makes sure that the process is killed when switched.
        OS.createProcess(new Write());
        OS.getScheduler().switchProcess();
        for (int i = 0; i <= 20; i++) {
            OS.createProcess(new Memory()); // tests 20 instances of Memory.
            OS.getScheduler().switchProcess();
        }
    }

}
