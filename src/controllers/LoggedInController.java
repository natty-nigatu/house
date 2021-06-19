package controllers;

import data.Category;
import data.Location;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Agent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import models.House;
import program.Main;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LoggedInController implements Initializable {

    boolean newHouse = false;

    @FXML
    private VBox listingList;

    @FXML
    private Button btnNewListing;

    @FXML
    private TextField txtTitle;

    @FXML
    private ImageView imageView;

    @FXML
    private Button btnSaveChanges;

    @FXML
    private Button btnUploadImage;

    @FXML
    private Button btnDeleteListing;

    @FXML
    private ComboBox<String> comboCategory;

    @FXML
    private ComboBox<String> comboLocation;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextArea txtFeatures;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnUpdateInfo;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnReset;


    ListingListener listingListener;
    Agent agent;
    House chosenHouse;

    @FXML
    void resetHandler(ActionEvent event) {
        loadAccount();
    }

    boolean confirmDelete = false;

    @FXML
    void deleteListingHandler(ActionEvent event) {

        if(newHouse)
            return;

        if(!confirmDelete){
            btnDeleteListing.setText("Confirm Delete");
            confirmDelete = true;

            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run () {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    confirmDelete = false;
                                    btnDeleteListing.setText("Delete Listing");
                                }
                            });
                        }
                    }, 2000);

            return;
        }

        try {
            Main.server.deleteHouse(chosenHouse);
            reload();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @FXML
    void logoutHandler(ActionEvent event) {
        try {

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/home.fxml"));
            primaryStage.setTitle("Login");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

            Stage stage = (Stage)btnLogout.getScene().getWindow();
            stage.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void newListingHandler(ActionEvent event) {

        newHouse = true;
        chosenHouse = new House();
        chosenHouse.setAgent(agent);

        txtTitle.clear();
        imageView.setImage(null);
        comboCategory.getSelectionModel().select(1);
        comboLocation.getSelectionModel().select(1);
        txtPrice.clear();
        txtFeatures.clear();
    }

    @FXML
    void reloadHandler(ActionEvent event) {
        reload();
    }

    @FXML
    void saveChangesHandler(ActionEvent event) {

        if (!check())
            return;

        chosenHouse.setTitle(txtTitle.getText());
        chosenHouse.setPrice(Integer.parseInt(txtPrice.getText()));
        chosenHouse.setCategory(comboCategory.getSelectionModel().getSelectedIndex());
        chosenHouse.setLocation(comboLocation.getSelectionModel().getSelectedIndex());
        chosenHouse.setFeatures(txtFeatures.getText());


        try {
            if (newHouse) {
                Main.server.addHouse(chosenHouse);
                reload();
            }
            else
                Main.server.saveHouse(chosenHouse);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }


        btnSaveChanges.setText("Saved");

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                btnSaveChanges.setText("Save Changes");
                            }
                        });
                    }
                }, 2000);

    }

    @FXML
    void updateInfoHandler(ActionEvent event) {

        agent.setName(txtName.getText());
        agent.setPhone(Integer.parseInt(txtPhone.getText()));
        agent.setEmail(txtEmail.getText());

        if(txtPassword.getText().length() > 0)
            agent.setPassword(txtPassword.getText());


        try {
            Main.server.saveAgent(agent);
        }catch (Exception ex){
            ex.printStackTrace();
            return;
        }

        btnUpdateInfo.setText("Saved");

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run () {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                btnUpdateInfo.setText("Update Information");
                            }
                        });
                    }
                }, 2000);
    }

    @FXML
    void uploadImageHandler(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        //file extension filter to only accept image files
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("Image Files ", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(btnUploadImage.getScene().getWindow());
        if (file == null) {
            return;
        }

        chosenHouse.setImage(file);
        chosenHouse.setImageid(getFileExtension(file));

        imageView.setImage(chosenHouse.getImage());
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf("."));
        else return "";
    }

    private boolean check(){


        if(txtTitle.getText().isBlank()){
            btnSaveChanges.setText("Please Set Title");
            setSaveText();
            return false;
        }

        if(txtPrice.getText().isBlank()){
            btnSaveChanges.setText("Please Set Price");
            setSaveText();
            return false;
        }

        if(imageView.getImage() == null){
            btnSaveChanges.setText("Please Upload an Image");
            setSaveText();
            return false;
        }

        return true;
    }

    private void setSaveText(){

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                btnSaveChanges.setText("Save Changes");
                            }
                        });
                    }
                }, 1000);
    }


    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
        loadAccount();
        reload();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboCategory.setItems(Category.getAll());
        comboLocation.setItems(Location.getAll());
    }

    private void clearView(){
        txtTitle.setText("");
        imageView.setImage(null);
        comboCategory.getSelectionModel().select(1);
        comboLocation.getSelectionModel().select((1));
        txtPrice.setText("");
        txtFeatures.setText("");
    }

    private void reload(){

        listingList.getChildren().clear();

        List<House> houseList = getData();

        if (houseList.size() > 0 ) {

            if (newHouse)
                setChosenListing(houseList.get(houseList.size() - 1));
            else
                setChosenListing(houseList.get(0));

            listingListener = new ListingListener() {
                @Override
                public void onClickListener(House house) {
                    setChosenListing(house);
                }
            };
        } else {
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

        newHouse = false;
        if (confirmDelete) {
            confirmDelete = false;
            btnDeleteListing.setText("Delete Listing");
        }
        chosenHouse = house;

        txtTitle.setText(chosenHouse.getTitle());
        imageView.setImage(chosenHouse.getImage());
        comboCategory.getSelectionModel().select(chosenHouse.getCategory());
        comboLocation.getSelectionModel().select(chosenHouse.getLocation());
        txtPrice.setText(Integer.toString(chosenHouse.getPrice()));
        txtFeatures.setText(chosenHouse.getFeatures());
    }

    private List<House> getData(){

        try{
            return Main.server.getListingsbyAgent(agent.getId());

        } catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    private void loadAccount(){
        try {
            agent = Main.server.loadAgent(agent.getId());

            txtName.setText(agent.getName());
            txtPhone.setText(Integer.toString(agent.getPhone()));
            txtEmail.setText(agent.getEmail());
            txtUsername.setText(agent.getUsername());


        }catch (Exception ex){
            ex.printStackTrace();
        }
        }
}
