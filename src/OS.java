/**
 * Name: Rusho Binnabi
 * Date: 1/17/2024
 * Assignment: 1 - The Beginning
 * Class: ICSI 412 - Spring 2024
 */

import java.util.*;

public class OS {

    // this class is the gateway between the userland thread and the kernel thread.

    private static VirtualToPhysicalMapping virtualToPhysicalMapping;
    private static final Random rng = new Random();
    private static final Kernel kernel = Kernel.getKernelInstance(); // creates a private, static reference to the one and only instance of the Kernel class.
    private static Scheduler scheduler = new Scheduler();
    private static final ArrayList<Object> functionParameters = new ArrayList<>();
    private static Object returnValue;

    private static IdleProcess idleProcess;

    private static final HashMap<Integer, PCB> kernelandProcesses = new HashMap<>();

    /**
     * this getVirtualToPhysicalMapping() method gets the virtual to physical mapping.
     * @return the virtual to physical mapping.
     */
    public static VirtualToPhysicalMapping getVirtualToPhysicalMapping() {
        return virtualToPhysicalMapping;
    }

    private static PCB pcb = new PCB(new UserlandProcess() {
        @Override
        public void main() {

        }
    });

    private static KernelMessage kernelMessage;

    private static final HashMap<Integer, PCB> waitingProcesses = new HashMap<>();

    /**
     * this getRng() method gets the random number generator.
     * @return the random number generator.
     */
    public static Random getRng() {
        return rng;
    }

    /**
     * this getWaitingProcesses() method gets the hashmap of waiting processes.
     * @return the hashmap of waiting processes.
     */

    public static HashMap<Integer, PCB> getWaitingProcesses() {
        return waitingProcesses;
    }

    /**
     * this getKernelMessage() method gets the KernelMessage object.
     * @return the KernelMessage object.
     */

    public static KernelMessage getKernelMessage() {
        return kernelMessage;
    }

    /**
     * this getPcb() method gets the PCB.
     * @return the PCB.
     */

    public static PCB getPcb() {
        return pcb;
    }

    /**
     * this getKernelandProcesses() gets the hashmap of kerneland processes.
     * @return the hashmap of kerneland processes.
     */
    public static HashMap<Integer, PCB> getKerlandProcesses() {
        return kernelandProcesses;
    }

    /**
     * this OS() constructor initializes the objects and variables.
     */
    public OS() {

    }

    /**
     * this setIdleProcess() method sets the idle process.
     * @param idleProcess the idle process being set.
     */

    public static void setIdleProcess(IdleProcess idleProcess) {
        OS.idleProcess = idleProcess;
    }

    /**
     * this getIdleProcess() method gets the idle process.
     * @return the idle process.
     */

    public static IdleProcess getIdleProcess() {
        return idleProcess;
    }


    /**
     * this setScheduler() method sets the scheduler.
     * @param scheduler the scheduler being set.
     */

    public static void setScheduler(Scheduler scheduler) {
        OS.scheduler = scheduler;
    }

    /**
     * this getScheduler() method gets the scheduler.
     * @return the scheduler.
     */

    public static Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * this getKernel() method gets the kernel.
     * @return the kernel.
     */

    public static Kernel getKernel() {
        return kernel;
    }

    /**
     * this CallType enum is an enum of a function to be called.
     */

    public enum CallType {
        CreateProcess,
        SwitchProcess,
        PriorityProcess,
        SleepProcess
    }

    private static CallType currentCall;

    /**
     * this getCurrentCall() method gets the currently called function.
     * @return the currently called function.
     */

    public static CallType getCurrentCall() {
        return currentCall;
    }

    /**
     * this setCurrentCall() method sets the currently called function.
     * @param currentCall the currently called function being set.
     */

    public static void setCurrentCall(CallType currentCall) {
        OS.currentCall = currentCall;
    }

    /**
     * this getFunctionParameters() method gets the list of function parameters for the function.
     * @return the list of function parameters for the function.
     */
    public static ArrayList<Object> getFunctionParameters() {
        return functionParameters;
    }

    public static void setFunctionParameters(Object functionParameter) {
        functionParameters.add(functionParameter);
    }

    /**
     * this getReturnValue() method gets the return value.
     * @return the return value.
     */

    public static Object getReturnValue() {
        return returnValue;
    }

    /**
     * this setReturnValue() method sets the return value.
     * @param returnValue the return value being set.
     */

    public static void setReturnValue(Object returnValue) {
        OS.returnValue = returnValue;
    }

    /**
     * this createProcess() method makes an enum entry for CreateProcess and handles each kernel by:
     *
     * 1: resetting the parameters.
     * 2: adding the new parameters to the list of parameters.
     * 3: setting the currently called function.
     * 4: switching to the kernel.
     * 5: casting and returning the return value.
     *
     * @param up the userland process being created.
     * @return the return value.
     */

    public static int createProcess(UserlandProcess up) {
        setFunctionParameters(up);
        getFunctionParameters().remove(up);
        setCurrentCall(CallType.valueOf("CreateProcess"));
        getKernel().getSemaphore().release();
        getKernel().start();
        getKerlandProcesses().put(getPID(), getScheduler().getCurrentlyRunning()); // puts the kernelland processes into the hashmap.
        if (getScheduler().getCurrentlyRunning() != null) {
            setReturnValue(getScheduler().getCurrentlyRunning());
            getKernel().getScheduler().getCurrentlyRunning().stop();
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
        System.out.println(getReturnValue());
        return 0;
    }

    /**
     * this new version of the createProcess() method does pretty much the same thing as the old version
     * except this version uses both UserlandProcess and a new enum for Priority for the OS.
     * @param up the userland process.
     * @param callType the priority enum.
     * @return the return value.
     */

    public static int createProcessNew(UserlandProcess up, CallType callType) {
        setFunctionParameters(up);
        getFunctionParameters().remove(up);
        setCurrentCall(callType);
        getKernel().getSemaphore().release();
        getKernel().start();
        if (getScheduler().getCurrentlyRunning() != null) {
            setReturnValue(getScheduler().getCurrentlyRunning());
            getKernel().getScheduler().getCurrentlyRunning().stop();
            getKernel().sleep(250);
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
        System.out.println(getReturnValue());
        return 0;
    }

    /**
     * this startup() method creates the Kernel() object and calls the createProcess() method twice,
     * once for init and once for the idle process.
     * @param init the init process.
     */

    public static void startup(UserlandProcess init) {
        getKernel().start();
        createProcess(init);
        createProcess(getIdleProcess());
    }

    /**
     * this sleep() method creates the Enum for the sleep, puts the parameters in the list,
     * switches to the kernel, gets the return value, etc., when processes get put to sleep.
     * @param milliseconds the time for processes to sleep in milliseconds.
     */

    public static void sleep(int milliseconds) {
        setFunctionParameters(milliseconds);
        getFunctionParameters().remove(getScheduler().getCurrentlyRunning());
        setCurrentCall(CallType.valueOf("SleepProcess"));
        getKernel().getSemaphore().release();
        if (getScheduler().getCurrentlyRunning() != null) {
            setReturnValue(getScheduler().getCurrentlyRunning());
            getKernel().getScheduler().getCurrentlyRunning().stop();
        }
        else {
            try {
                while (true) {
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                System.out.println("Error. There was nothing sleeping.");
            }
            System.out.println(getReturnValue());
        }
    }

    /**
     * this getPID() method gets the current process' pid.
     * @return the current process' pid.
     */

    public static int getPID() {
        return getScheduler().getCurrentlyRunning().getProcessIDPID();
    }

    /**
     * this getPIDByName() method gets the pid of a process with that name.
     * @param processPID the name of the process.
     * @return the pid of a process with that name.
     */

    public static int getPIDByName(String processPID) {
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

    public static void sendMessage(KernelMessage km) {
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
        getKernel().sendMessage(km);
    }

    /**
     * this waitForMessage() method waits for a message to get sent to it before a process runs.
     * @return the kernel message.
     */

    public static KernelMessage waitForMessage() {
        if (getKernelMessage().getWhatMessage() != 0) {
            return getPcb().getMessageQueue().remove();
        }
        else {
            getWaitingProcesses().put(getKernelMessage().getWhatMessage(), getPcb());
            return null;
        }
    }

    /**
     * this getMapping() method maps the virtual page number with the entries in the tlb?
     *
     * @param virtualPageNumber the index into the array of integers.
     * @return a random process.
     */

    public static PCB getMapping(int virtualPageNumber) {
        if (getVirtualToPhysicalMapping().getPhysicalPageNumber() == -1) {
            for (int i = 0; i < getPcb().getMemoryMap().length; i++) {
                getPcb().getMemoryMap()[i] = getVirtualToPhysicalMapping();
                getPcb().getUserlandProcess().write(i, getPcb().getUserlandProcess().read(getVirtualToPhysicalMapping().getPhysicalPageNumber())); // talks to the kernel and is in the getMapping()
                                                                                                                                                    // method because its a kernel call.
            }
        }
        return getScheduler().getRandomProcess();
    }
}