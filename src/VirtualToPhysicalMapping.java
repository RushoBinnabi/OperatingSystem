/**
 * Name: Rusho Binnabi
 * Date: 3/30/2024
 * Assignment: 6 - Virtual Memory
 * Class: ICSI 412 - Spring 2024
 */

public class VirtualToPhysicalMapping {

    // this class is the mapping between virtual and physical page numbers and on disk page numbers.

    public int physicalPageNumber;

    public int diskPageNumber;

    /**
     * this getPhysicalPageNumber() method gets the physical page number.
     * @return the physical page number.
     */
    public int getPhysicalPageNumber() {
        return physicalPageNumber;
    }

    /**
     * this setPhysicalPageNumber() method sets the physical page number.
     * @param i the physical page number being set.
     */

    public void setPhysicalPageNumber(int i) {
        physicalPageNumber = i;
    }

    /**
     * this getDiskPageNumber() method gets the disk page number.
     * @return the disk page number.
     */

    public int getDiskPageNumber() {
        return diskPageNumber;
    }

    /**
     * this setDiskPageNumber() method sets the disk page number.
     * @param i the disk page number being set.
     */

    public void setDiskPageNumber(int i) {
        diskPageNumber = i;
    }

    /**
     * this VirtualToPhysicalMapping() constructor sets the physical page
     * number and disk page numbers to -1.
     */

    public VirtualToPhysicalMapping() {
        setPhysicalPageNumber(-1);
        setDiskPageNumber(-1);
    }
}
