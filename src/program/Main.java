package program;

import actions.DatabaseAction;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {

    public static void main(String[] args) {


        try {
            Message.server("Connecting to Database . . .");
            DatabaseAction db = new DatabaseAction();
            db.createConnection();

           // DatabaseAction stub = (DatabaseAction) UnicastRemoteObject.exportObject(db,0);

            Message.server("Starting Database Server . . .");

            Registry registry = LocateRegistry.createRegistry(8000);
            registry.rebind("database", db);

            Message.server("Database Server is Up and Running.");


        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
