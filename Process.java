import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Process extends UnicastRemoteObject implements RMIInterface {
    int counter;
    int logicalTime;
    int id;
    int t = 6;
    int key = 4;
    Scanner sc = new Scanner(System.in);
    List<RMIInterface> processList = new ArrayList<>(Arrays.asList(null, null, null, null, null));
    public BlockingQueue<Message> processQueue = new LinkedBlockingQueue<Message>();

    public Process(int id) throws RemoteException {
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
            processQueue.put(m);
            //System.out.println("Message added! Sender: "+sender);
        } catch (InterruptedException e) {
            System.out.println("Message error:: Process Queue");
            e.printStackTrace();
        }
    }

    public void runProcess() {
        System.out.println("Process " + id + " Started!");
        // The machines
        String name = "//in-csci-rrpc0" + id + ".cs.iupui.edu:4444/Process" + id;
        RMIInterface rmiInterface = this;

        //Binding
        try {
            Naming.rebind(name, rmiInterface);
        } catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }

        System.out.println("Process " + id + " Stub name: " + name + "\n");
        System.out.println("------------------------------------------");
        System.out.println("Do you wish to connect to all the processes and master?");
        System.out.println("1. yes");
        System.out.println("2. no");
        int choice = sc.nextInt();
        // Getting the interface for processes
        if (choice == 1) {
            for (int i = 1; i < 5; i++) {
                if (i != id) {
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
            }
            // connecting to the master
            name = "//in-csci-rrpc05.cs.iupui.edu:4444/Master";
            try {
                RMIInterface rmiinterface = (RMIInterface) Naming.lookup(name);
                if (rmiinterface == null) {
                    System.out.println("Lookup Error: Master");
                }
                processList.set(4, rmiinterface);
            } catch (Exception e) {
                System.out.println("Error:: Master");
                e.printStackTrace();
                System.exit(-1);
            }
            System.out.println("Connection Established :: Master");
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
        }

        while (true) {
            int n = ThreadLocalRandom.current().nextInt(1, 4);
            switch (n) {
                case 1:
                    try {
                        sendMessage();
                    } catch (RemoteException e) {
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter++;
            iterator++;
            // System.out.println(id+","+iterator+","+logicalTime);
            if (iterator == 1000)
                break;

        }
    }

    // receive function
    private void receiveMessage() {
        if (processQueue.isEmpty()) {
            logicalTime++;
        } else {
            Message m = processQueue.remove();
            // if sender is master then change the logical clock based on the offset
            // else it is the process communication, use lamport to adjust clock
            if (m.getSender() == 5) {
                System.out.println("---------------------------------------");
                System.out.println("Process " + id + " iteration " + iterator + " Current logical Clock " + logicalTime + " [" + id + "] offset " + m.getTime());
                logicalTime = logicalTime - m.getTime() + 1;
                System.out.println("Process " + id + " iteration " + iterator + " Changed logical Clock " + logicalTime);
                System.out.println("---------------------------------------");
            } else {
                System.out.println("Sender Logical Clock: " + m.getTime());
                System.out.print("Receive Event :: Process " + id + " Current Logical Clock " + logicalTime);
                // lamport clock adjustment
                logicalTime = Math.max(m.getTime(), logicalTime) + 1;
                System.out.println(" Changed logical Clock " + logicalTime);
            }

            // Byzantine or arbitrary failures: If the counter mod some random value is 0 then byzantine failure occurs
            if (counter % ThreadLocalRandom.current().nextInt(100, 200) == 0) {
                logicalTime = logicalTime / 2;
                System.out.println("Process " + id + " Byzantine Failure occurred, Logical clock changed: " + logicalTime);
            }
        }
    }

    // This is send function: if the counter crosses time t it sends it's clock to master for adjustment
    // Else it communicates with other process
    private void sendMessage() throws RemoteException {
        if (counter % t == 0) {
            System.out.println("---------------------------------------");
            // 4 is for master
            RMIInterface target = (RMIInterface) processList.get(4);
            // Encryption using the key
            target.putMessage(logicalTime+key, id);
            System.out.println("Message send to Master by " + id);
            System.out.println("---------------------------------------");
        }
        int n = ThreadLocalRandom.current().nextInt(0, 4);
        if (n == (id - 1))
            internalEvent();
        else {
            // Encryption using the key
            RMIInterface target = processList.get(n);
            target.putMessage(logicalTime+key, id);
            System.out.println("Send Event :: Process " + id + " :: to Process " + (n + 1));
        }
    }

    // This is internal event
    private void internalEvent() {
        logicalTime++;
        System.out.println("Internal Event :: Process " + id + " :: Logical Time " + logicalTime);
    }


}