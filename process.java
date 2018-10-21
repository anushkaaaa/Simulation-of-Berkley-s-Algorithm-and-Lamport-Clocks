/**
 * Honor Pledge:
 * I pledge that I have neither given nor received any help on this assignment.
 * ahpatil
 */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

// This class maintains the 4 processes
public class process extends Thread {
    //blocking queue
    BlockingQueue<message> processQueue = new LinkedBlockingQueue<message>();
    process[] array_objects;
    int counter;
    int t = 6;
    int logicalTime;
    int pid;
    master m;

    // constructor
    public process(int counter, int logicalTime, int pid, process[] array_objects, master m) {
        this.counter = counter;
        this.logicalTime = logicalTime;
        this.pid = pid;
        this.array_objects = array_objects;
        this.m = m;
    }

    // iterator to terminate the while
    int iterator = 0;

    // The run function:
    // Here an integer n is randomly selected: the randomly selected event takes place per iteration
    @Override
    public void run() {
        while (true) {
            int n = ThreadLocalRandom.current().nextInt(1, 4);
            switch (n) {
                case 1:
                    try {
                        sendMessage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    receiveMessage();
                    break;
                default:
                    internalEvent();
                    break;
            }
            counter++;
            iterator++;
            //System.out.println(pid+","+iterator+","+logicalTime);
            if (iterator == 1000)
                break;

        }
    }

    // receive function
    private void receiveMessage() {
        if (processQueue.isEmpty()) {
            logicalTime++;
        } else {
            message m = processQueue.remove();
            // if sender is master then change the logical clock based on the offset
            // else it is the process communication, use lamport to adjust clock
            if (m.getSender() == 5) {
                System.out.println("[" + pid + "] iteration " + iterator + " Current logical Clock " + logicalTime + " [" + pid + "] offset " + m.getTime());
                logicalTime = logicalTime - m.getTime() + 1;
                System.out.println("[" + pid + "]  iteration " + iterator + " Changed logical Clock " + logicalTime);
            } else {
                System.out.println("Sender Logical Clock: " + m.getSender());
                System.out.println("Receive Event :: Process " + pid + " Current Logical Clock " + logicalTime);
                // lamport clock adjustment
                logicalTime = Math.max(m.getTime(), logicalTime) + 1;
                System.out.println("[" + pid + "] Changed logical Clock " + logicalTime);
            }

            // Byzantine or arbitrary failures: If the counter mod some random value is 0 then byzantine failure occurs
            if (counter % ThreadLocalRandom.current().nextInt(100, 200) == 0) {
                logicalTime = logicalTime / 2;
                System.out.println("[" + pid + "] Byzantine Failure occurred, Logical clock changed: " + logicalTime);
            }
        }
    }

    // This is send function: if the counter crosses time t it sends it's clock to master for adjustment
    // Else it communicates with other process
    private void sendMessage() throws InterruptedException {
        if (counter % t == 0) {
            m.masterQueue.put(new message(logicalTime, pid));
            System.out.println("Message send to Master by " + pid);
        }
        int n = ThreadLocalRandom.current().nextInt(1, 5);
        if (n == pid)
            internalEvent();
        else {
            process target = array_objects[n - 1];
            target.processQueue.put(new message(logicalTime, pid));
        }
        System.out.println("Send Event :: Process " + pid + " :: to Process " + n);
    }

    // This is internal event
    private void internalEvent() {
        logicalTime++;
        System.out.println("Internal Event :: Process " + pid + " :: Logical Time " + logicalTime);
    }

}
