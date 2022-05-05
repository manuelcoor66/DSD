import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Idonaciones extends Remote {
    public int getDonaciones(int idCliente) throws RemoteException, NotBoundException;

    public int getDonacionesReplica() throws RemoteException, NotBoundException;

    public int registrarCliente(int idCliente) throws RemoteException, NotBoundException;

    public void donar(int cantidad, int idCliente) throws RemoteException;
}