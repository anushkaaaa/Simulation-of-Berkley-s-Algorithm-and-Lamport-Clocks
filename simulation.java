/**
 * Honor Pledge:
 * I pledge that I have neither given nor received any help on this assignment.
 * ahpatil
 */
// This is the simulation file
public class simulation {

    // an arrayobject of processes will be used for referencing the processes
    process array_objects[] = new process[4];

    public static void main(String args[]) {
        new simulation().start();
    }

    // the start function
    void start() {
        // creation of master thread
        master m = new master(0, 0, array_objects);
        Thread master = new Thread(m);
        master.start();
        // creation of 4 process threads
        for (int i = 1; i <= 4; i++) {
            array_objects[i - 1] = new process(0, 0, i, array_objects, m);
            Thread t = new Thread(array_objects[i - 1]);
            t.start();
        }
    }

}
