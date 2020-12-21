package Controller;
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

public class LoginController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField username_field;

    @FXML
    private TextField password_text_field;

    @FXML
    private Button login_button;

    @FXML
    private Button cancel_button;

    @FXML
    private Label location_label;

    @FXML
    private PasswordField password_field;

    @FXML
    void cancel_button_action(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program?");
        alert.setHeaderText("Are you sure you wish to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    @FXML
    void login_button_action(ActionEvent event) throws IOException{
        Alert alert;
        if (!password_text_field.getText().isEmpty() && !username_field.getText().isEmpty()) {
            try {
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                Connection myConn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ05k6r", "U05k6r", "53688529325");
                Statement myStmt = myConn.createStatement();
                ResultSet myRs = myStmt.executeQuery("select * from users WHERE User_Name ='"+ username_field.getText() +"' AND Password = '"+ password_text_field.getText() +"'");
                if (myRs.next()) {
                //String password = myRs.getString("Password");
                //String username = myRs.getString("User_Name");

                //ResultSet myRs = myStmt.executeQuery("SELECT * FROM users");
                //while(myRs.next()){
                    /*System.out.println(myRs.getString("User_Name"));
                    System.out.println(myRs.getString("Password"));

                    System.out.println(username_field.getText());
                    System.out.println(password_text_field.getText());*/

                    /*String password = myRs.getString("Password");
                    String username = myRs.getString("User_Name");

                    String myPassword = password_text_field.getText();
                    String myUsername = username_field.getText();*/

                    /*alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Success");
                    alert.setContentText("Information is correct");
                    alert.showAndWait();*/

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

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("/view/mainScreen.fxml"));
                    loader.load();
                    this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    Parent scene = (Parent)loader.getRoot();
                    this.stage.setScene(new Scene(scene));
                    this.stage.show();

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Information is incorrect");
                    alert.showAndWait();

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

                /*if(myRs.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Success");
                    alert.setContentText("Information is correct");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Either username or password are incorrect.");
                    alert.showAndWait();
                }*/
                //System.out.println(myRs.getString("User_Name"));
                //   }
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please fill in all blanks");
            alert.showAndWait();
        }
            /*System.out.println(username_text_field);
            try {
                System.out.println(username_text_field);
                String username_field = username_text_field.toString();
                String password_field = password_text_field.toString();
                Connection myConn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ05k6r", "U05k6r", "53688529325");
                Statement myStmt = myConn.createStatement();
                ResultSet myRs = myStmt.executeQuery("select * from users WHERE User_Name ='"+ username_field +"' AND Password = '"+ password_field +"'");
                *//*while (myRs.next()) {
                    String password = myRs.getString("Password");
                    String username = myRs.getString("User_Name");*//*

                if(myRs.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Success");
                    alert.setContentText("Information is correct");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Either username or password are incorrect.");
                    alert.showAndWait();
                }
                    //System.out.println(myRs.getString("User_Name"));
             //   }
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }
        }*/
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String string = ZoneId.systemDefault().toString();
        location_label.setText(string);

    }
}
