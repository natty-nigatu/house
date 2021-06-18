package program;

import interfaces.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.House;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Main extends Application {

    public static Server server;
    public static String host = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/home.fxml"));
        primaryStage.setTitle("Rent a House");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {

        try {
            Message.client("Connecting To Server . . . ");
            Registry registry = LocateRegistry.getRegistry(host, 8080);
            server = (Server) registry.lookup("server");
            Message.client("Successfully Connected to Server.");
        } catch (Exception ex){
            ex.printStackTrace();
        }

        launch(args);


    }
}
