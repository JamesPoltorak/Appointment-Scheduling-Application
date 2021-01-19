package Controller;
import Model.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import javax.swing.text.ZoneView;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.*;

import static Model.Application.upcomingAppointment;

/**
 * Controls the login screen
 */
public class LoginController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Label title;

    @FXML
    private TextField username_field;

    @FXML
    private Button login_button;

    @FXML
    private Button cancel_buttton;

    @FXML
    private Label username_label;

    @FXML
    private Label password_label;

    @FXML
    private Label current_location_label;

    @FXML
    private Label location_label;

    @FXML
    private TextField password_text_field;

    public static String username;


    /**
     * cancels the login controller
     * @param event
     */
    @FXML
    private void cancel_button_action(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program?");
        alert.setHeaderText("Are you sure you wish to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    /**
     * login to the main screen
     * @param event
     * @throws IOException
     */
    @FXML
    private void login_button_action(ActionEvent event) throws IOException{
        Alert alert;
        if (!password_text_field.getText().isEmpty() && !username_field.getText().isEmpty()) {
            try {
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                Connection myConn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ05k6r", "U05k6r", "53688529325");
                Statement myStmt = myConn.createStatement();
                ResultSet myRs = myStmt.executeQuery("select * from users WHERE User_Name ='"+ username_field.getText() +"' AND Password = '"+ password_text_field.getText() +"'");
                if (myRs.next()) {

                    try(FileWriter fw = new FileWriter("login_activity.txt", true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter out = new PrintWriter(bw))
                    {
                        out.println("SUCCESSFUL LOGIN:");
                        out.println("Username: " + username_field.getText());
                        out.println("Password: " + password_text_field.getText());
                        out.println("At: " + formatter.format(date));
                    } catch (IOException e) {

                    }

                    username = username_field.getText();
                    upcomingAppointment();

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("/view/mainScreen.fxml"));
                    loader.load();
                    this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    Parent scene = (Parent)loader.getRoot();
                    this.stage.setScene(new Scene(scene));
                    this.stage.show();

                } else {
                    Application.wrongInfo();

                    try(FileWriter fw = new FileWriter("login_activity.txt", true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter out = new PrintWriter(bw))
                    {
                        out.println("ATTEMPTED LOGIN:");
                        out.println("Attempted username: " + username_field.getText());
                        out.println("Attempted password: " + password_text_field.getText());
                        out.println("At: " + formatter.format(date));
                    } catch (IOException e) {
                    }
                }
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }
        } else {
            Application.missingInfo();
        }
    }


    /**
     * intializes the login controller
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String string = ZoneId.systemDefault().toString();
        location_label.setText(string);



        if(Application.isFrench(Application.defaultLocale)){
            title.setText("Application de planification de calendrier");
            username_label.setText("Nom d'utilisateur");
            password_label.setText("Mot de passe");
            username_field.setPromptText("Entrez votre nom d'utilisateur ici");
            password_text_field.setPromptText("Entrez votre mot de passe ici");
            login_button.setText("Le Login");
            cancel_buttton.setText("Annuler");
            current_location_label.setText("Localisation actuelle:");
        }

    }
}
