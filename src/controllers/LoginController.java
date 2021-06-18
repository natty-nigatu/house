package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Agent;
import program.Main;

public class LoginController {
    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblInfo;

    @FXML
    private Button btnBack;

    @FXML
    void backHandler(ActionEvent event) {

        try {

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../views/home.fxml"));
            primaryStage.setTitle("Rent a House");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

            Stage stage = (Stage)btnBack.getScene().getWindow();
            stage.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    void loginHandler(ActionEvent event) {

        if (txtUsername.getText().length() == 0 || txtPassword.getText().length() == 0){
            lblInfo.setText("Please Fill all Fields");
            return;
        }

        try {

            int status = Main.server.login(txtUsername.getText(), txtPassword.getText());

            if (status == 0){
                lblInfo.setText("Invalid Username or Password.");
                return;
            }
            else{

                try {

                    Stage primaryStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("../views/loggedin.fxml"));
                    Parent root = fxmlLoader.load();
                    primaryStage.setTitle("Rent a House");

                    LoggedInController controller = (LoggedInController)fxmlLoader.getController();
                    Agent agent = Main.server.loadAgent(status);
                    controller.setAgent(agent);

                    Scene scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.setMaximized(true);
                    primaryStage.show();

                    Stage stage = (Stage)btnBack.getScene().getWindow();
                    stage.close();

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
