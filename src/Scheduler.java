/**
 * Name: Rusho Binnabi
 * Date: 1/17/2024
 * Assignment: 1 - The Beginning
 * Class: ICSI 412 - Spring 2024
 */

import java.time.Clock;
import java.util.*;

public class Scheduler {

    // this class is the Scheduler.

    private final Random random = new Random();

    private final Queue<PCB> realTimeProcesses = new LinkedList<>();
    private final Queue<PCB> interactiveProcesses = new LinkedList<>();
    private final Queue<PCB> backgroundProcesses = new LinkedList<>();

    private final Queue<PCB> sleepingProcesses = new LinkedList<>();
    private final Clock clock = Clock.systemUTC();
    private int currentClockValue;

    private Timer timer = new Timer();

    private VirtualToPhysicalMapping virtualToPhysicalMapping;
    public UserlandProcess currentlyRunning = new PCB(new UserlandProcess() {
        @Override
        public void main() {

        }
    }).getUserlandProcess();

    /**
     * this getVirtualToPhysicalMapping() method gets the virtual to physical mapping.
     * @return the virtual to physical mapping.
     */
    public VirtualToPhysicalMapping getVirtualToPhysicalMapping() {
        return virtualToPhysicalMapping;
    }

    public Random getRandom() {
        return random;
    }

    public Queue<PCB> getBackgroundProcesses() {
        return backgroundProcesses;
    }

    public Queue<PCB> getInteractiveProcesses() {
        return interactiveProcesses;
    }

    public Queue<PCB> getRealTimeProcesses() {
        return realTimeProcesses;
    }

    public Queue<PCB> getSleepingProcesses() {
        return sleepingProcesses;
    }

    /**
     * this getClock() method gets the clock.
     * @return the clock.
     */

    public Clock getClock() {
        return clock;
    }

    /**
     * this setCurrentClockValue() method adds the requested time for processes to sleep to the current clock value
     * which will be the minimum time for processes to wake up.
     * @param currentClockValue the requested, minimum time for processes to sleep and to later wake up.
     */

    public void setCurrentClockValue(int currentClockValue) {
        this.currentClockValue += currentClockValue;
    }

    /**
     * this getCurrentClockValue() method gets the current clock value which is
     * the requested, minimum time for processes to sleep and to later wake up.
     * @return the current clock value.
     */

    public int getCurrentClockValue() {
        return currentClockValue;
    }


    /**
     * this setTimer() method sets the timer.
     * @param timer the timer being set.
     */

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    /**
     * this getTimer() method gets the timer.
     * @return the timer.
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * this getCurrentlyRunning() method gets the currently running process.
     * @return the currently running process.
     */
    public PCB getCurrentlyRunning() {
        return new PCB(currentlyRunning);
    }

    /**
     * this setCurrentlyRunning() method sets the currently running process.
     * @param up the userland process currently being set.
     */
    public void setCurrentlyRunning(UserlandProcess up) {
        currentlyRunning = up;
    }

    /**
     * this Scheduler() constructor initializes the objects and variables.
     */
    public Scheduler() {
        getTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                getCurrentlyRunning().getUserlandProcess().requestStop(true);
            }
        }, 250);
    }

    /**
     * this createProcess() method adds the userland process to the list of processes and, if nothing else is running,
     * then it calls the switchProcess() method on it to start it.
     * @param up the userland process being created.
     * @return 0.
     */

    public int createProcess(UserlandProcess up) {
        if (!getCurrentlyRunning().getUserlandProcess().isStopped()) {
            getInteractiveProcesses().add(new PCB(up));
        }
        else {
            switchProcess();
        }
        return 0;
    }

    /**
     * this new version of the createProcess() method does pretty much the same thing as the old version
     * except this version uses both UserlandProcess and a new enum for Priority for the Scheduler.
     * @param up the userland process.
     * @param callType the priority enum.
     * @return the return value.
     */

    public int createProcess(UserlandProcess up, OS.CallType callType) {
        if (!getCurrentlyRunning().getUserlandProcess().isStopped()) {
            getInteractiveProcesses().add(new PCB(up));
            OS.getFunctionParameters().remove(up);
            OS.setCurrentCall(callType);
            OS.getKernel().start();
            OS.getKerlandProcesses().put(getPID(), getCurrentlyRunning()); // puts the kernelland processes into the hashmap.
            if (OS.getScheduler().getCurrentlyRunning() != null) {
                OS.setReturnValue(OS.getScheduler().getCurrentlyRunning());
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
        }
        else {
            getCurrentlyRunning().getUserlandProcess().incrementPriorityVariable(1);
            switchProcess();
        }
        return (int) OS.getReturnValue();
    }

    /**
     * this switchProcess() method takes the currently running process and puts it on the
     * end of the list of processes. It then takes the head of the list and runs it.
     */

    public void switchProcess() {
        if (getCurrentlyRunning().getUserlandProcess().getPriorityVariable() == 5) {
            OS.getKerlandProcesses().remove(getPID(), getCurrentlyRunning()); // removes the kernelland processes into the hashmap.
            demote();
        }
        OS.sleep(250);
        setCurrentClockValue(250);
        getSleepingProcesses().add(new PCB(getCurrentlyRunning().getUserlandProcess()));
        if (!getSleepingProcesses().isEmpty()) {
            for (int i = 0; i < getSleepingProcesses().size(); i++) {
                if (getCurrentClockValue() >= getClock().millis()) { // checks to see what sleeping processes should be woken up.
                    setCurrentlyRunning(getSleepingProcesses().iterator().next().getUserlandProcess());
                    getCurrentlyRunning().run();
                }
            }
        }
        for (int i = 0; i < OS.getPcb().getMemoryMap().length; i++) {
            OS.getKernel().getFreeSpace()[i] = false;
        }
        Arrays.fill(UserlandProcess.getTlb(), null); // clears the TLB.
    }

    /**
     * this demote() method demotes the processes from real-time -> interactive -> background.
     */

    public void demote() {
        switch (1 + getRandom().nextInt(3)) { // for adding the 3 types of processes to the correct queue.
            case 1:
                if (getCurrentlyRunning().getUserlandProcess().isStopped()) {
                    getInteractiveProcesses().add(new PCB(getCurrentlyRunning().getUserlandProcess()));
                }
                break;
            case 2:
                if (getCurrentlyRunning().getUserlandProcess().isStopped()) {
                    getBackgroundProcesses().add(new PCB(getCurrentlyRunning().getUserlandProcess()));
                }
                break;
            case 3:
                if (getCurrentlyRunning().getUserlandProcess().isStopped()) {
                    getRealTimeProcesses().add(new PCB(getCurrentlyRunning().getUserlandProcess()));
                }
                break;
        }
        switch (1 + getRandom().nextInt(3)) { // to determine the correct queue to pull from and for removing the first item from the queue.
            case 1:
                if (getCurrentlyRunning().getUserlandProcess().isStopped()) {
                    getInteractiveProcesses().remove();
                }
                break;
            case 2:
                if (getCurrentlyRunning().getUserlandProcess().isStopped()) {
                    getBackgroundProcesses().remove();
                }
                break;
            case 3:
                if (getCurrentlyRunning().getUserlandProcess().isStopped()) {
                    getRealTimeProcesses().remove();
                }
                break;
        }
    }

    /**
     * this sleep() method creates the Enum for the sleep, puts the parameters in the list,
     * switches to the kernel that switches processes, gets the return value, etc., when
     * processes get put to sleep.
     * @param milliseconds the time for processes to sleep in milliseconds.
     */

    public void sleep(int milliseconds) {
        OS.setFunctionParameters(milliseconds);
        OS.getFunctionParameters().remove(milliseconds);
        OS.setCurrentCall(OS.CallType.valueOf("Sleep"));
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

    public static int getPIDByName(String processPID) {
        OS.getScheduler().getCurrentlyRunning().setName(processPID);
        if (OS.getScheduler().getCurrentlyRunning().getName().equals(processPID)) {
            return getPID();
        }
        else {
            return 0;
        }
    }

    /**
     * this getRandomProcess() method does a page swap by getting a random process
     * and tries to find a page in the process that has physical memory.
     * @return the random process from the PCB that has the page within the process
     * that has physical memory.
     */

    public PCB getRandomProcess() {
        int i = getRandom().nextInt(getVirtualToPhysicalMapping().getDiskPageNumber());
        while (i != 0) {
            if (i == 0) {
                i = getRandom().nextInt(getVirtualToPhysicalMapping().getDiskPageNumber());
            }
            getVirtualToPhysicalMapping().setDiskPageNumber(i);
            getVirtualToPhysicalMapping().setPhysicalPageNumber(-1);
            if (UserlandProcess.getTlb()[getVirtualToPhysicalMapping().getDiskPageNumber()][getVirtualToPhysicalMapping().getPhysicalPageNumber()] != i) { // if the TLB didn't have the data that was needed.
                if (getVirtualToPhysicalMapping().getDiskPageNumber() != -1) {
                    getVirtualToPhysicalMapping().setPhysicalPageNumber(getVirtualToPhysicalMapping().getDiskPageNumber()); // "steals" a page from the process and updates the mapping of virtual -> physical mapping.
                }
                else if (getVirtualToPhysicalMapping().getDiskPageNumber() == 0){
                    getVirtualToPhysicalMapping().setPhysicalPageNumber(0);
                }
                UserlandProcess.getPhysicalMemory()[i] = getCurrentlyRunning().getUserlandProcess().read(getVirtualToPhysicalMapping().getPhysicalPageNumber()); // reuses physical memory load.
            }
            else { // the else statement manages the excess blocks of memory for the swap file.
                OS.getKernel().seek(i, getVirtualToPhysicalMapping().getDiskPageNumber());
                OS.getKernel().setPageNumber(OS.getKernel().write(i, OS.getKernel().read(i, getVirtualToPhysicalMapping().getDiskPageNumber())));
                getVirtualToPhysicalMapping().setPhysicalPageNumber(i);
                getVirtualToPhysicalMapping().setDiskPageNumber(OS.getKernel().getPageNumber()); // updates the virtual to physical page mapping.
                UserlandProcess.getTlb()[i][getVirtualToPhysicalMapping().getPhysicalPageNumber()] = i; // updates the TLB.
            }
        }
        return new PCB(getCurrentlyRunning().getUserlandProcess());
    }
}