package controllers;

import data.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.House;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private ComboBox<String> comboCategory;

    @FXML
    private VBox listingList;

    @FXML
    private Label lblTitle;

    @FXML
    private ImageView imageView;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblFeatures;

    @FXML
    private Label lblAgentName;

    @FXML
    private Label lblAgentPhone;

    @FXML
    private Label lblAgentEmail;

    ListingListener listingListener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        comboCategory.setItems(Category.getAll());
        reload(1);

    }

    private void reload(int category){
        List<House> houseList = getData(category);

        if (houseList.size() > 1 ) {
            setChosenListing(houseList.get(1));

            listingListener = new ListingListener() {
                @Override
                public void onClickListener(House house) {
                    setChosenListing(house);
                }
            };
        }

        for (House house : houseList) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../view/listing.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                StackPane stack = new StackPane(anchorPane);
                stack.setStyle("-fx-background-color: white; -fx-background-radius: 40px;" +
                        " -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");

                VBox.setMargin(stack, new Insets(0, 30, 0, 40));

                ListingController listingController = fxmlLoader.getController();
                listingController.setData(house, listingListener);

                listingList.getChildren().add(stack);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
    private void setChosenListing(House house){

        lblTitle.setText(house.getTitle());
        imageView.setImage(house.getImage());
        lblCategory.setText(Category.get(house.getCategory()));
        lblPrice.setText(Integer.toString(house.getPrice()));
    }

    private List<House> getData(int category){

        List<House> list = new ArrayList<>();

        House house = new House();
        house.setTitle("House 1 Apartment iasldkfjaskldfjas faklafsj ");
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
        house.setTitle("Home iasldkfjaskldfjas faklafsj ");
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
        house.setTitle("Warehouse iasldkfjaskldfjas faklafsj ");
        house.setCategory(4);
        house.setPrice(12345);
        house.setImage(new File("src/img/5.jpg"));
        list.add(house);

        return list;

    }
}
