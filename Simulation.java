public class Simulation {
    public static void main(String[] args) {
        int objectType = -1;
        int objectId = -1;

        if (args.length <= 1) {
            System.out.println("Please provide the Object Type and Object Id");
            System.exit(-1);
        } else {
            // 0 - master 1- process
            objectType = Integer.parseInt(args[0]);
            // object id
            objectId = Integer.parseInt(args[1]);
        }
        if (objectType == 0) {
            // master
            try {
                Master master = new Master(objectId);
                master.runMaster();
            } catch (Exception e) {
                System.out.println("Remote Exception " + e);
            }
        } else if (objectType == 1) {
            // process
            try {
                Process process = new Process(objectId);
                process.runProcess();
            } catch (Exception e) {
                System.out.println("Remote Exception " + e);
            }
        } else {
            System.out.println("Please provide valid Object Type");
            System.exit(-1);
        }
    }
}
