/**
 * Name: Rusho Binnabi
 * Date: 1/18/2024
 * Assignment: 1 - The Beginning
 * Class: ICSI 412 - Spring 2024
 */

import java.util.concurrent.Semaphore;

public abstract class IdleProcess extends UserlandProcess {

    // this class runs an infinite loop of the cooperate() method and thread sleep.

    /**
     * empty IdleProcess() constructor.
     */

    public IdleProcess() {

    }

    /**
     * this main() method gets implemented from the UserlandProcess class and creates an infinite loop
     * of calling the cooperate() method and thread sleep.
     */

    @Override
    public void main() {
        try {
            while (true) {
                cooperate();
                Thread.sleep(50);
            }
        }
        catch (Exception e) {
            System.out.println("Error. The infinite loop of the cooperate() method and thread sleep could not be run.");
        }
    }
}
