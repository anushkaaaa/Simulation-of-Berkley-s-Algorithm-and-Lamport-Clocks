public class Message {
    int time;
    int sender;

    //constructor
    public Message(int time, int sender) {
        this.time = time;
        this.sender = sender;
    }

    //getter actions
    public int getTime() {
        return time;
    }

    public int getSender() {
        return sender;
    }

}