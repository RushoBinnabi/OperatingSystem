/**
 * Name: Rusho Binnabi
 * Date: 2/28/2024
 * Assignment: 4 - Messages
 * Class: ICSI 412 - Spring 2024
 */

import java.util.Arrays;

public class KernelMessage {

    // this KernelMessage class will handle the messages that sent back and forth between the sender and the target.

    private final int senderPID;
    private final int targetPID;
    private final int whatMessage;
    byte[] dataArray;

    /**
     * this getSenderPID() method gets the sender pid.
     * @return the sender pid.
     */

    public int getSenderPID() {
        return senderPID;
    }

    /**
     * this getTargetPID() method gets the target pid.
     * @return the target pid.
     */

    public int getTargetPID() {
        return targetPID;
    }

    /**
     * this getWhatMessage() gets the integer that indicates what the message is.
     * @return the integer that indicates what the message is.
     */

    public int getWhatMessage() {
        return whatMessage;
    }

    /**
     * this getDataArray() method gets the byte array that is whatever the applications want it to be.
     * @return the byte array that is whatever the applications want it to be.
     */

    public byte[] getDataArray() {
        return dataArray;
    }

    public KernelMessage(int senderPID, int targetPID, int whatMessage, byte[] dataArray) {
        this.senderPID = senderPID;
        this.targetPID = targetPID;
        this.whatMessage = whatMessage;
        this.dataArray = dataArray;
    }

    /**
     * this KernelMessage() copy constructor gets a Kernel message and makes a copy of it.
     * @param kernelMessage the Kernel message.
     */

    public KernelMessage(KernelMessage kernelMessage) {
        this.senderPID = kernelMessage.getSenderPID();
        this.targetPID = kernelMessage.getTargetPID();
        this.whatMessage = kernelMessage.getWhatMessage();
        this.dataArray = kernelMessage.getDataArray();
    }

    /**
     * this toString() method creates a string representation of the sender and target PIDs,
     * the integer for the message type, and the byte array for the application type.
     * @return a string representation of the sender and target PIDs,
     * the integer for the message type, and the byte array for the application type.
     */

    public String toString() {
        return "Sender PID: " + getSenderPID() + " " + "Target PID: " + getTargetPID() + " " +
                "Message Type: " + getWhatMessage() + " " + "Application Type: " + Arrays.toString(getDataArray()) + " ";
    }
}
