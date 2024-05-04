/**
 * Name: Rusho Binnabi
 * Date: 3/20/2024
 * Assignment: 5 - Paging
 * Class: ICSI 412 - Spring 2024
 */
public class Read extends UserlandProcess {

    // this Read test class is for reading from memory.

    /**
     * empty Read() constructor.
     */
    public Read() {

    }

    /**
     * this main() method gets implemented from the UserlandProcess class and
     * tries to read from memory in an infinite loop and calls the cooperate() method.
     */

    @Override
    public void main() {
        while (true) {
            byte read = read(100 * 1024);
            System.out.println("Bytes read from memory: " + read);
            cooperate();
        }
    }
}
