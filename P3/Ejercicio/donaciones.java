import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class donaciones extends UnicastRemoteObject implements Idonaciones {
    Registry registry;
    replicas replica;

    public donaciones(String host, replicas replica) throws RemoteException {
        this.replica = replica;
    }

    public synchronized int getDonaciones(int idCliente) throws RemoteException, NotBoundException {
        return replica.getDonaciones(idCliente);
    }

    public synchronized int getDonacionesReplica() throws RemoteException, NotBoundException {
        return replica.getDonacionesReplica();
    }

    public synchronized int registrarCliente(int idCliente) throws RemoteException, NotBoundException {
        return replica.registrarCliente(idCliente);
    }

    public synchronized void donar(int cantidad, int idCliente) throws RemoteException {
        replica.donar(cantidad, idCliente);
    }
}