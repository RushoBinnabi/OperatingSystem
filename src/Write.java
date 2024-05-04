/**
 * Name: Rusho Binnabi
 * Date: 3/20/2024
 * Assignment: 5 - Paging
 * Class: ICSI 412 - Spring 2024
 */
public class Write extends UserlandProcess {

    // this Write test class is for writing to memory.

    /**
     * empty Write() constructor.
     */
    public Write() {

    }

    /**
     * this main() method gets implemented from the UserlandProcess class and
     * tries to write to memory and calls the cooperate() method.
     */

    @Override
    public void main() {
        while (true) {
            System.out.println("Bytes written to memory: \n");
            write(3100, (byte) 69);
            cooperate();
        }
    }
}
