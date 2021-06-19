package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Location {

    private static String [] list = {"All", "Addis Ababa", "Adama", "Mekele", "Bahir Dar", "Hawassa", ""};

    public static String get(int i){

        if (i < list.length)
            return list[i];

        return "";
    }

    public static ObservableList<String> getAll(){

        ObservableList<String> list = FXCollections.observableArrayList();
        String temp;

        for (int i = 0 ; true; i++){
            temp = get(i);
            if(temp.equals(""))
                break;

            else list.add(temp);
        }

        return list;
    }
}
