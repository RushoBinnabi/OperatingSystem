/**
 * Name: Rusho Binnabi
 * Date: 3/20/2024
 * Assignment: 5 - Paging
 * Class: ICSI 412 - Spring 2024
 */
public class Memory extends UserlandProcess {

    // this Memory test class is for allocating and freeing memory.

    private PCB pcb;

    private Kernel kernel;

    /**
     * empty Memory() constructor.
     */
    public Memory() {

    }

    /**
     * this getKernel() method gets the kernel.
     * @return the kernel.
     */

    public Kernel getKernel() {
        return kernel;
    }

    /**
     * this getPcb() method gets the PCB.
     * @return the PCB.
     */

    public PCB getPcb() {
        return pcb;
    }

    /**
     * this main() method gets implemented from the UserlandProcess class and
     * tries to allocate and free memory, and calls the cooperate() method.
     */

    @Override
    public void main() {
        VirtualToPhysicalMapping memory = getKernel().allocateMemory(100 * 1024); // tests using more than the allocated memory.
        System.out.println("Amount of memory allocated: " + memory);
        byte readMemory;
        while (true) {
            try {
                readMemory = read(3100);
                getPcb().getUserlandProcess().write(readMemory, (byte) 69);
                if (getKernel().freeMemory(memory.getPhysicalPageNumber(), readMemory)) {
                    System.out.println("Amount of memory freed: " + readMemory);
                }
            }
            catch (Exception e) {
                System.out.println("Error. Memory could not be freed.");
            }

        }
    }
}
