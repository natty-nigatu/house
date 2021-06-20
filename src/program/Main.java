package program;

import actions.ServerAction;
import interfaces.Database;
import models.House;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    static String host = "localhost";

    public static void main(String[] args) {

        try {
            Message.server("Connecting to Database Server . . .");
            Registry registry = LocateRegistry.getRegistry(host, 8000);
            Database db = (Database)registry.lookup("database");

            Message.server("Successfully Connected to Database Server.");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

            Message.server("Starting this Server . . .");
            ServerAction server = new ServerAction(db);

            Registry registry2 = LocateRegistry.createRegistry(8080);
            registry2.rebind("server", server);

            Message.server("Server is Up and Running.");

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
