import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class cliente implements Runnable {
    private String host;
    private String replica;

    public cliente(String host, String replica) {
        this.host = host;
        this.replica = replica;
    }

    public static void main(String args[]) {
        String host = "localhost";
        String replica = "";
        String clientes;
        Scanner in = new Scanner(System.in);
        ArrayList<cliente> Clientes = new ArrayList<>();
        ArrayList<Thread> vectorHebras = new ArrayList<>();
        int nClientes = 0 ;

        System.out.print("\nElija a que réplica quiere conectarse [0,1]: ");
        replica = in.nextLine();

        while (!replica.equals("0") && !replica.equals("1")) {
            System.out.print("Se ha introducido el número de una réplica no existente, introduzca uno correcto [0,1]: ");
            replica = in.nextLine();
        } 

        System.out.print("\nIndica la cantidad de clientes que se van registrar: ");
        clientes = in.nextLine();
        nClientes = Integer.parseInt(clientes);
        System.out.print("\n");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        for (int i = 0 ; i < nClientes; i++) {
            Clientes.add(new cliente(host, replica));
        }

        for (int i = 0 ; i < nClientes ; i++) {
            vectorHebras.add(new Thread(Clientes.get(i), Integer.toString(i)));
            vectorHebras.get(i).start();
        }
    }

    @Override
    public void run() {
        try {
            Registry registry = LocateRegistry.getRegistry(this.host, 1099);
            int donacion = 0;

            System.out.println("El cliente con id " + Thread.currentThread().getName() + " se está registrando en en la réplica número " + this.replica);
            Idonaciones local = (Idonaciones) registry.lookup("Replica" + this.replica + "Donaciones");
            this.replica = Integer.toString(local.registrarCliente(Integer.parseInt(Thread.currentThread().getName())));
            System.out.println("El cliente con id " + Thread.currentThread().getName() + " se ha registrado en la réplica número " + this.replica);

            donacion = new Random().nextInt(100);
            System.out.println("El cliente con id " + Thread.currentThread().getName() + " ha donado la cantidad de " + donacion + " en la réplica número " + this.replica);
            local = (Idonaciones) registry.lookup("Replica" + this.replica + "Donaciones");
            local.donar(donacion, Integer.parseInt(Thread.currentThread().getName()));
            System.out.println("Total donado en la réplica " + this.replica + ": " + local.getDonacionesReplica());
            System.out.println("La donación del cliente con id " + Thread.currentThread().getName() + " se ha terminado");

            System.out.println("Total donado en la réplica " + this.replica + ": " + local.getDonacionesReplica());
            } catch (Exception e) {
            System.err.println("Error en el cliente " + Thread.currentThread().getName());
            e.printStackTrace();
        }
    }
}