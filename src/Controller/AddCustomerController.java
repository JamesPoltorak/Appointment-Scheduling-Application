package Controller;

import Model.Application;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static Controller.LoginController.username;
import static Model.Application.*;

/**
 * AddCustomerController controls the add customer screen
 */
public class AddCustomerController implements Initializable {

    Stage stage;

    @FXML
    private ComboBox<String> customer_country;

    @FXML
    private ComboBox<String> customer_state;

    @FXML
    private TextField customer_ID;

    @FXML
    private TextField customer_name;

    @FXML
    private TextField customer_address;

    @FXML
    private TextField customer_post_code;

    @FXML
    private TextField customer_phone;

    private int custID;
    int country = 0;

    /**
     * cancels the add customer
     * @param event
     * @throws IOException
     */
    @FXML
    void cancel_button(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/mainScreen.fxml"));
        loader.load();
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();
    }

    /**
     * Saves the customer
     * @param event
     * @throws IOException
     */
    @FXML
    void save_button(ActionEvent event) throws IOException {
        Alert alert;
        this.custID = customerInc;

        if(!this.customer_name.getText().isEmpty() && !this.customer_address.getText().isEmpty() && this.customer_state.getValue() != null && this.customer_country.getValue() != null && !this.customer_post_code.getText().isEmpty() && !this.customer_phone.getText().isEmpty()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            LocalDateTime now = LocalDateTime.now();
            String today = now.format(formatter);

            String custName = this.customer_name.getText();
            String custAddress = this.customer_address.getText();
            Integer custPost = Integer.parseInt(this.customer_post_code.getText());
            Long custPhone = Long.parseLong(this.customer_phone.getText());


            if(customer_country.getValue().equals("Canada")) {
                country = CANCountries.get(customer_state.getValue());
            }
            else if(customer_country.getValue().equals("United Kingdom")) {
                country = UKCountries.get(customer_state.getValue());
            }
            else if(customer_country.getValue().equals("United States")) {
                country = USCountries.get(customer_state.getValue());
            }

            Application.addCustomer(new Customer(this.custID, this.customer_name.getText(), this.customer_address.getText(), Integer.parseInt(this.customer_post_code.getText()),Long.parseLong(this.customer_phone.getText()), this.country));
            customerIDCount();
            customerID.add(custID);
            try {
                Connection myConn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ05k6r", "U05k6r", "53688529325");
                Statement myStmt = myConn.createStatement();
                myStmt.execute("INSERT INTO customers VALUES ('"+custID+"','"+custName+"','"+custAddress+"','"+custPost+"','"+custPhone+"',now(),'"+username+"',now(),'"+username+"','"+country+"')");
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/view/mainScreen.fxml"));
            loader.load();
            this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = (Parent)loader.getRoot();
            this.stage.setScene(new Scene(scene));
            this.stage.show();
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Missing Information");
            alert.setContentText("Please fill in all the blanks.");
            alert.showAndWait();
        }

    }

    /**
     * intializes the add customer screen
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {

        Application.data.put("Canada", Application.countryCAN);
        Application.data.put("United Kingdom", Application.countryUK);
        Application.data.put("United States", Application.countryUS);

        customer_country.getItems().removeAll(customer_country.getItems());
        customer_country.getItems().addAll("Canada", "United Kingdom","United States");

        customer_country.valueProperty().addListener((obs, oldValue, newValue) -> {
            List<String> list = Application.data.get(newValue);
            if(list != null) {
                customer_state.setDisable(false);
                customer_state.getItems().setAll(list);
            } else {
                customer_state.getItems().clear();
                customer_state.setDisable(true);
            }
        });

    }
}
