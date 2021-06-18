package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;

public class Category {

    public static String get(int i){

        switch (i){
            case 0:
                return "All";

            case 1:
                return "Office";

            case 2:
                return "House";

            case 3:
                return "Apartment";

            case 4:
                return "Warehouse";
        }

        return "";
    }

    public static ObservableList<String> getAll(){

        ObservableList<String> list = FXCollections.observableArrayList();
        String temp;

        for (int i = 0 ; true; i++){
            temp = get(i);
            if(temp == "")
                break;

            else list.add(temp);
        }

        return list;
    }
}
