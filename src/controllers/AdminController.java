package controllers;

import data.Category;
import data.Location;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Agent;
import models.House;
import program.Main;

import java.net.URL;
import java.util.*;

public class AdminController implements Initializable {

    private Agent agent;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
        loadAccount();
    }

    @FXML
    private ComboBox<String> comboLocation;

    @FXML
    private ComboBox<String> comboCategory;

    @FXML
    private ComboBox<String> comboAgentType;

    @FXML
    private VBox listingList;

    @FXML
    private Label lblTitle;

    @FXML
    private ImageView imageView;

    @FXML
    private Button btnDeleteListing;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblLocation;

    @FXML
    private Label lblFeatures;

    @FXML
    private Label lblAgentName;

    @FXML
    private Label lblAgentPhone;

    @FXML
    private Label lblAgentEmail;

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

    boolean confirmDelete = false;

    @FXML
    void deleteListingHandler(ActionEvent event) {
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
    void locationHandler(ActionEvent event) {
        reload();
    }

    @FXML
    void logoutHandler(ActionEvent event) {
        try {

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/home.fxml"));
            primaryStage.setTitle("Rent a House");
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
    void resetHandler(ActionEvent event) {
        loadAccount();
    }

    @FXML
    void typeHandler(ActionEvent event) {
        reload();
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

    ListingListener listingListener;
    private House chosenHouse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboCategory.setItems(Category.getAll());
        comboCategory.getSelectionModel().select(0);

        comboLocation.setItems(Location.getAll());
        comboLocation.getSelectionModel().select(0);

        String [] temp = {"Administrator", "Listing Agent"};

        comboAgentType.setItems(FXCollections.observableArrayList(temp));
        comboAgentType.getSelectionModel().select(1);

        reload();

        //tblagent
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        try {
            txtPhone.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if (!t1.matches("\\d*") || t1.length() > 9)
                        txtPhone.setText(s);
                }
            });
        }
        catch (Exception ex){}

        try {
            txtPhone1.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if (!t1.matches("\\d*") || t1.length() > 9)
                        txtPhone1.setText(s);
                }
            });
        }
        catch (Exception ex){}


        loadAgents();
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

        if (confirmDelete) {
            confirmDelete = false;
            btnDeleteListing.setText("Delete Listing");
        }

        chosenHouse = house;

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

    //////////////////////////////////////////////////////////////////////////////////////////////////////// agent tab

    @FXML
    private TextField txtName1;

    @FXML
    private TextField txtPhone1;

    @FXML
    private TextField txtEmail1;

    @FXML
    private TextField txtUsername1;

    @FXML
    private PasswordField txtPassword1;

    @FXML
    private Button btnAddAgent;

    @FXML
    private Button btnReloadAgents;

    @FXML
    private TableView<Agent> tblAgents;

    @FXML
    private TableColumn<Agent, String> colName;

    @FXML
    private TableColumn<Agent, Integer> colPhone;

    @FXML
    private TableColumn<Agent, String> colEmail;

    @FXML
    private TableColumn<Agent, String> colUsername;

    @FXML
    void reloadagentHandler(ActionEvent event) {
        loadAgents();
    }

    private void loadAgents(){
        try{

            tblAgents.setItems(FXCollections.observableArrayList(Main.server.getAgents()));

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void addAgentHandler(ActionEvent event) {

        if(txtName1.getText().isBlank() || txtPhone1.getText().isBlank() || txtEmail1.getText().isBlank() ||
            txtUsername1.getText().isBlank() || txtPassword1.getText().isBlank()){

            btnAddAgent.setText("Please Fill All Fields First");
            resetAddAgent();
            return;
        }

        try{

            if(!Main.server.testUsername(txtUsername1.getText().trim())) {
                btnAddAgent.setText("Username is Already Taken");
                resetAddAgent();
                return;
            }

        } catch (Exception ex){
            ex.printStackTrace();
            return;
        }

        Agent agent1 = new Agent();
        agent1.setName(txtName1.getText());
        agent1.setPhone(Integer.parseInt(txtPhone1.getText()));
        agent1.setEmail(txtEmail1.getText());
        agent1.setUsername(txtUsername1.getText().trim());
        agent1.setPassword(txtPassword1.getText().trim());
        agent1.setType(comboAgentType.getSelectionModel().getSelectedIndex());

        try{
            Main.server.addAgent(agent1);

            btnAddAgent.setText("Agent Added");
            resetAddAgent();

            txtName1.clear();
            txtPhone1.clear();
            txtEmail1.clear();
            txtUsername1.clear();
            txtPassword1.clear();
            comboAgentType.getSelectionModel().select(1);

            loadAgents();

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void resetAddAgent(){
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run () {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                btnAddAgent.setText("Add Agent");
                            }
                        });
                    }
                }, 2000);
    }

}
