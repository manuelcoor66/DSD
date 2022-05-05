import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class replicas extends UnicastRemoteObject implements Ireplicas {
    private Ireplicas replica;
    private Registry registry;
    private int idReplica;
    private int numReplicas;
    private Map<Integer, Integer> usuarios = new HashMap<Integer, Integer>(); 
    private int subTotal = 0;

    public replicas(String host, int idReplica, int numReplicas) throws RemoteException {
        registry = LocateRegistry.getRegistry(host, 1099);
        this.idReplica = idReplica;
        this.numReplicas = numReplicas;
    }

    public synchronized int getDonaciones(int id) throws RemoteException, NotBoundException {
        int total = 0;

        if (usuarios.containsKey(id) && usuarios.get(id) > 0) {
            total = subTotal;
            for (int i = 0 ; i < this.numReplicas ; i++) {
                if (i != this.idReplica) {
                    replica = (Ireplicas) registry.lookup("Replica" + i);
                    total += replica.getDonacionesReplica();
                }
            }
        }

        return total;
    }

    public synchronized int getDonacionesReplica() throws RemoteException {
        return subTotal;
    }

    public synchronized int registrarCliente(int id) throws RemoteException, NotBoundException {
        int replicaRegistro = idReplica;
        int numUsuarios;
        int minnumUsuarios = getTotalusuarios();
        boolean registrado = false;

        for (int i = 0 ; i < this.numReplicas && !registrado ; i++) {
            if (i != this.idReplica) {
                replica = (Ireplicas) registry.lookup("Replica" + i);

                registrado = replica.estaRegistrado(id);

                numUsuarios = replica.getTotalusuarios();
                if (numUsuarios < minnumUsuarios) {
                    replicaRegistro = i;
                    minnumUsuarios = numUsuarios;
                }
            }
        }

        if (!registrado) {
            if (replicaRegistro != idReplica) {
                replica = (Ireplicas) registry.lookup("Replica" + replicaRegistro);
                this.replica.registrar(id);
            }
            else {
                registrar(id);
            }
        }

        return replicaRegistro;
    }

    public synchronized void registrar(int id) throws RemoteException, NotBoundException {
        this.usuarios.put(id, 0);
    }

    public synchronized boolean estaRegistrado(int id) throws RemoteException {
        return usuarios.containsKey(id);
    }

    public synchronized int getTotalusuarios() throws RemoteException {
        return usuarios.size();
    }

    public synchronized void donar(int cantidad, int id) throws RemoteException {
        if (estaRegistrado(id)) {
            usuarios.put(id, cantidad);
            subTotal += cantidad;
            System.out.println("El cliente " + id + " ha donado la cantidad de " + cantidad + " en la réplica número " + idReplica);
        }
        else {
            System.out.println("El cliente " + id + " no está registrado y por lo tanto no puede donar");
        }
    }
}