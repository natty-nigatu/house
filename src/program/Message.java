package program;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Message {

    public static void server(String message){

        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " [DATABASE SERVER] " + message);
    }

    public static void client(String message){

        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " [CLIENT SERVER] " + message);
    }
}
