import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Ireplicas extends Remote {
    public int getDonaciones(int id) throws RemoteException, NotBoundException;

    public int getDonacionesReplica() throws RemoteException;

    public int registrarCliente(int id) throws RemoteException, NotBoundException;

    public void registrar(int id) throws RemoteException, NotBoundException;

    public boolean estaRegistrado(int id) throws RemoteException;

    public int getTotalusuarios() throws RemoteException;

    public void donar(int cantidad, int id) throws RemoteException;
}