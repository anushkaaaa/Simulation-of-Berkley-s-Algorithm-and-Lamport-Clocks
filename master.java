import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.StrictMath.max;

/**
 * Honor Pledge:
 * I pledge that I have neither given nor received any help on this assignment.
 * ahpatil
 */
// This is the master class, for working of Berkeley Algorithm
public class master extends Thread {
    int counter;
    int logicalTime;
    process[] array_objects;
    BlockingQueue<message> masterQueue = new LinkedBlockingQueue<message>();

    // list to maintain the logical clocks of the processes, initialized to null
    List<Integer> masterTimeList = new ArrayList<>(Arrays.asList(null, null, null, null));

    // constructor
    public master(int counter, int logicalTime, process[] array_objects) {
        this.counter = counter;
        this.logicalTime = logicalTime;
        this.array_objects = array_objects;
    }

    // iterator to terminate the while
    int iterator = 0;

    // Here, the master thread performs 3 events: internal event, receive event and send event
    @Override
    public void run() {
        while (true) {
            try {
                internalEvent();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            receiveMessage();
            if (check()) {
                try {
                    sendMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else
                logicalTime++;
            iterator++;
            //System.out.println("5,"+iterator+","+logicalTime);
            if (iterator == 1000)
                break;
        }
    }

    // This function : working of berkeley's Algorithm
    private void sendMessage() throws InterruptedException {
        // computing average of all logical clocks
        int avg = logicalTime;
        for (int i = 0; i < 4; i++) {
            avg += masterTimeList.get(i);
        }
        avg = avg / 5;
        System.out.println("------------------Average by master object : " + avg + " -----------------");
        int offset;
        // computing the offset for each process
        for (int i = 0; i < 4; i++) {
            offset = masterTimeList.get(i) - avg;
            process target = array_objects[i];
            target.processQueue.put(new message(offset, 5));
            masterTimeList.set(i, null);
        }
        // master's offset
        offset = logicalTime - avg;
        System.out.println("Master :: Current Logical clock : " + logicalTime + " offset " + offset);
        logicalTime = logicalTime - offset + 1;
        System.out.print("Master :: changed Logical clock : " + logicalTime + "\n");
        counter++;
    }

    // This is the receive function: receive all the logical clocks and store it in the arraylist
    private void receiveMessage() {
        if (masterQueue.isEmpty()) {
            logicalTime++;
        } else {
            message m = masterQueue.remove();
            int sender = m.getSender();
            int senderLogicalClock = m.getTime();
            masterTimeList.set(sender - 1, senderLogicalClock);
            // Lamport clock adjustment
            logicalTime = max(logicalTime,m.getTime()) + 1;
        }
        counter++;
    }

    //This is the internal event
    private void internalEvent() throws InterruptedException {
        //master.sleep(0,1);
        counter++;
        logicalTime++;
        System.out.println("Internal Event :: Master :: Logical Time " + logicalTime);
    }

    // This boolean checks whether all logical clocks are received
    boolean check() {
        for (int i = 0; i < 4; i++) {
            if (masterTimeList.get(i) == null) {
                return false;
            }
        }
        return true;
    }
}
