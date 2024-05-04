/**
 * Name: Rusho Binnabi
 * Date: 1/17/2024
 * Assignment: 1 - The Beginning
 * Class: ICSI 412 - Spring 2024
 */

import java.util.concurrent.Semaphore;

public class HelloWorld extends UserlandProcess {

    // this class is one of the two test programs.

    /**
     * empty HelloWorld() constructor.
     */
    public HelloWorld() {

    }

    /**
     * this main() method gets implemented from the UserlandProcess class and prints hello world in an infinite loop
     * and calls the cooperate() method.
     */

    @Override
    public void main() {
        while (true) {
            System.out.println("Hello World");
            cooperate();
        }
    }
}
