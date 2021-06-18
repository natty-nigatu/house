package program;

import actions.DatabaseAction;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {


        try {
            DatabaseAction db = new DatabaseAction();
            db.createConnection();

            Registry registry = LocateRegistry.createRegistry(8000);
            registry.rebind("database", db);

            Message.server("Database Server is Up and Running.");


        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
