import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class servidor {
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            int numReplicas = 2;

            System.out.println("\nSe empiezan a iniciar todas las réplicas\n");

            for (int i = 0 ; i < numReplicas ; i++) {
                String remote = "Replica" + i;

                replicas replica = new replicas("localhost", i, numReplicas); 
                Naming.rebind(remote, replica);

                donaciones donaciones = new donaciones("localhost", replica); 
                Naming.rebind(remote+"Donaciones", donaciones);

                System.out.println("Se ha iniciado la réplica número " + i);
            }

            System.out.println("\nLas " + numReplicas + " réplicas se han iniciado correctamente\n");
            
        } catch (Exception e) {
            System.err.println("Replica exception:");
            e.printStackTrace();
        }
    }
}