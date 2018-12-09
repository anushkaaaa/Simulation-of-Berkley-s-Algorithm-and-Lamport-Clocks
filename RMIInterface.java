import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
	void putMessage(int time, int sender) throws RemoteException;
}