package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.House;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private VBox listingList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<House> list = getData();

        for(int i = 0; i < list.size(); i++){

            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../view/listing.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                StackPane stack = new StackPane(anchorPane);
                stack.setStyle("-fx-background-color: white; -fx-background-radius: 40px;" +
                        " -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");

                VBox.setMargin(stack, new Insets(0, 30, 0, 40));

                ListingController listingController = fxmlLoader.getController();
                listingController.setData(list.get(i));

                listingList.getChildren().add(stack);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    private List<House> getData(){

        List<House> list = new ArrayList<>();

        House house = new House();
        house.setTitle("Luxury Apartment iasldkfjaskldfjas faklafsj ");
        house.setCategory(1);
        house.setPrice(12345);
        house.setImage(new File("src/img/1.jpg"));
        list.add(house);

        house = new House();
        house.setTitle("Luxury Apartment iasldkfjaskldfjas faklafsj ");
        house.setCategory(2);
        house.setPrice(12345);
        house.setImage(new File("src/img/2.jpg"));
        list.add(house);

        house = new House();
        house.setTitle("Luxury Apartment iasldkfjaskldfjas faklafsj ");
        house.setCategory(3);
        house.setPrice(12345);
        house.setImage(new File("src/img/3.jpg"));
        list.add(house);

        house = new House();
        house.setTitle("Luxury Apartment iasldkfjaskldfjas faklafsj ");
        house.setCategory(4);
        house.setPrice(12345);
        house.setImage(new File("src/img/4.jpg"));
        list.add(house);

        house = new House();
        house.setTitle("Luxury Apartment iasldkfjaskldfjas faklafsj ");
        house.setCategory(5);
        house.setPrice(12345);
        house.setImage(new File("src/img/5.jpg"));
        list.add(house);

        return list;

    }
}
