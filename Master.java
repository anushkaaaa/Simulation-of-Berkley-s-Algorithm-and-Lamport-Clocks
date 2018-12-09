import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.StrictMath.max;

public class Master extends UnicastRemoteObject implements RMIInterface {
    int counter;
    int logicalTime;
    int id;
    int key = 4;
    public BlockingQueue<Message> masterQueue = new LinkedBlockingQueue<Message>();
    Scanner sc = new Scanner(System.in);

    // list to maintain the logical clocks of the processes, initialized to null
    List<Integer> masterTimeList = new ArrayList<>(Arrays.asList(null, null, null, null));
    List<RMIInterface> processList = new ArrayList<>(Arrays.asList(null, null, null, null));

    public Master(int id) throws RemoteException {
        this.id = id;
        this.logicalTime = 0;
        this.counter = 0;
    }

    // iterator to terminate the while
    int iterator = 0;

    @Override
    public void putMessage(int time, int sender) {
        try {
            // Decryption using the key
            time = time - key;
            Message m = new Message(time, sender);
            masterQueue.put(m);
            //	System.out.println("Message added! Sender: "+sender);
        } catch (InterruptedException e) {
            System.out.println("Message error:: Master Queue");
            e.printStackTrace();
        }
    }

    public void runMaster() {
        System.out.println("Master Started!");
        // The master is running on machine 10.234.136.59 in-csci-rrpc05.cs.iupui.edu
        String name = "//in-csci-rrpc0" + id + ".cs.iupui.edu:4444/Master";
        RMIInterface rmiInterface = this;

        //Binding
        try {
            Naming.rebind(name, rmiInterface);
        } catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }

        System.out.println("Stub name: " + name + "\n");
        System.out.println("------------------------------------------");
        System.out.println("Do you wish to connect to processes?");
        System.out.println("1. yes");
        System.out.println("2. no");
        int choice = sc.nextInt();
        // Getting the interface for processes
        if (choice == 1) {
            for (int i = 1; i < 5; i++) {
                name = "//in-csci-rrpc0" + i + ".cs.iupui.edu:4444/Process" + i;
                try {
                    RMIInterface rmiinterface = (RMIInterface) Naming.lookup(name);
                    if (rmiinterface == null) {
                        System.out.println("Lookup Error: process " + i);
                    }
                    processList.set(i - 1, rmiinterface);

                } catch (Exception e) {
                    System.out.println("Error:: Process " + i);
                    e.printStackTrace();
                    System.exit(-1);
                }
                System.out.println("Connection Established :: Process " + i);
            }
//
        }

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
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            logicalTime++;
            iterator++;
//            System.out.println("5,"+iterator+","+logicalTime);
//            if (iterator == 10000)
//                break;
        }
    }

    // This function : working of berkeley's Algorithm
    private void sendMessage() throws RemoteException {
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
            RMIInterface target = processList.get(i);
            // Encryption the time using key
            target.putMessage(offset+key, 5);
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
            Message m = masterQueue.remove();
            int sender = m.getSender();
            int senderLogicalClock = m.getTime();
            masterTimeList.set(sender - 1, senderLogicalClock);
            // Lamport clock adjustment
            logicalTime = max(logicalTime, m.getTime()) + 1;
            System.out.println("Master :: Received :: Process " + sender);
        }
        counter++;
    }

    //This is the internal event
    private void internalEvent() throws InterruptedException {
        counter++;
        logicalTime++;
        Thread.sleep(1000);
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