/**
 * Name: Rusho Binnabi
 * Date: 3/12/2024
 * Assignment: 4 - Messages
 * Class: ICSI 412 - Spring 2024
 */
public class Ping extends UserlandProcess {

    // this class is the Ping test class.

    /**
     * empty Ping() constructor.
     */
    public Ping() {

    }

    /**
     * this main() method gets implemented from the UserlandProcess class and prints Ping in an infinite loop
     * and calls the cooperate() method while also sending messages back and forth with Pong using its pid by name.
     */

    @Override
    public void main() {
        while (true) {
            System.out.println("Ping");
            int pid = OS.getPIDByName("Pong");
            var message = new KernelMessage(pid, pid, pid, new byte[10]);
            OS.sendMessage(message);
            OS.waitForMessage();
            cooperate();
        }
    }
}
