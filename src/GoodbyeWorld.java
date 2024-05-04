/**
 * Name: Rusho Binnabi
 * Date: 1/17/2024
 * Assignment: 1 - The Beginning
 * Class: ICSI 412 - Spring 2024
 */

import java.util.concurrent.Semaphore;

public class GoodbyeWorld extends UserlandProcess {

    // this class is the other test program.


    /**
     * empty GoodbyeWorld() constructor.
     */
    public GoodbyeWorld() {

    }

    /**
     * this main() method gets implemented from the UserlandProcess class and prints goodbye world in an infinite loop
     * and calls the cooperate() method.
     */

    @Override
    public void main() {
        while (true) {
            System.out.println("Goodbye World");
            cooperate();
        }
    }
}
