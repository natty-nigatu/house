package controllers;

import data.Category;
import data.Location;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.House;
import program.Main;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button btnLogin;

    @FXML
    private ComboBox<String> comboCategory;

    @FXML
    private ComboBox<String> comboLocation;

    @FXML
    private VBox listingList;

    @FXML
    private Label lblTitle;

    @FXML
    private ImageView imageView;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblLocation;

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
        comboCategory.getSelectionModel().select(0);

        comboLocation.setItems(Location.getAll());
        comboLocation.getSelectionModel().select(0);

        reload();

    }

    @FXML
    void locationHandler(ActionEvent event) {
        reload();
    }


    @FXML
    void typeHandler(ActionEvent event) {
        reload();
    }

    private void clearView(){
        lblTitle.setText("");
        imageView.setImage(null);
        lblCategory.setText("");
        lblLocation.setText("");
        lblPrice.setText("");
        lblFeatures.setText("");
        lblAgentPhone.setText("");
        lblAgentEmail.setText("");
        lblAgentName.setText("");
    }

    private void reload(){

        int category = comboCategory.getSelectionModel().getSelectedIndex();
        int location = comboLocation.getSelectionModel().getSelectedIndex();

        listingList.getChildren().clear();

        List<House> houseList = getData(category, location);

        if (houseList.size() > 0 ) {
            setChosenListing(houseList.get(0));

            listingListener = new ListingListener() {
                @Override
                public void onClickListener(House house) {
                    setChosenListing(house);
                }
            };
        } else
        {
            clearView();
        }

        for (House house : houseList) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../views/listing.fxml"));
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
        lblLocation.setText(Location.get(house.getLocation()));
        lblPrice.setText(Integer.toString(house.getPrice()));
        lblFeatures.setText(house.getFeatures());
        lblAgentName.setText(house.getAgent().getName());
        lblAgentEmail.setText(house.getAgent().getEmail());
        lblAgentPhone.setText(Integer.toString(house.getAgent().getPhone()));
    }

    private List<House> getData(int category, int location){

        if (category < 0)
            category = 0;

        if (location < 0)
            location = 0;

        try{
            return Main.server.getListings(category, location);

        } catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    @FXML
    void loginHandler(ActionEvent event) {

        try {

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/login.fxml"));
            primaryStage.setTitle("Login");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

            Stage stage = (Stage)btnLogin.getScene().getWindow();
            stage.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
