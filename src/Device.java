/**
 * Name: Rusho Binnabi
 * Date: 2/13/2024
 * Assignment: 3 - Devices
 * Class: ICSI 412 - Spring 2024
 */

import java.io.IOException;

public interface Device {

    // this Device interface makes it so that the operating system can work with devices by implementing this interface.

    int open(String s) throws Exception;
    void close(int id) throws IOException;
    byte[] read(int id, int size);
    void seek(int id, int to);
    int write(int id, byte[] data);
}