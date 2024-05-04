/**
 * Name: Rusho Binnabi
 * Date: 3/12/2024
 * Assignment: 4 - Messages
 * Class: ICSI 412 - Spring 2024
 */
public class Pong extends UserlandProcess {

    // this class is the Pong test class.

    /**
     * empty Pong() constructor.
     */
    public Pong() {

    }

    /**
     * this main() method gets implemented from the UserlandProcess class and prints Pong in an infinite loop
     * and calls the cooperate() method while also sending messages back and forth with Ping using its pid by name.
     */

    @Override
    public void main() {
        while (true) {
            System.out.println("Pong");
            int pid = OS.getPIDByName("Ping");
            var message = new KernelMessage(pid, pid, pid, new byte[10]);
            OS.sendMessage(message);
            OS.waitForMessage();
            cooperate();
        }
    }
}
