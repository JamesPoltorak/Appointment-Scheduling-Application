package Controller;

import Model.Application;
import Model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import static Controller.LoginController.username;
import static Model.Application.*;
import static java.time.ZoneOffset.UTC;

/**
 * Controls the modify appointment screen
 */
public class modifyAppointment implements Initializable {

    Stage stage;

    @FXML
    private TextField app_app_ID;

    @FXML
    private TextField app_title_field;

    @FXML
    private TextArea app_desc_field;

    @FXML
    private TextField app_location_field;

    @FXML
    private TextField app_type_field;

    @FXML
    private DatePicker app_date_field;

    @FXML
    private DatePicker app_end_date;

    @FXML
    private ComboBox<Integer> app_start_hour;

    @FXML
    private ComboBox<Integer> app_start_min;

    @FXML
    private ComboBox<Integer> app_end_hour;

    @FXML
    private ComboBox<Integer> app_end_min;

    @FXML
    private ComboBox<Integer> customer_ID_dropdown;

    @FXML
    private ComboBox<Integer> contact_id_dropdown;

    @FXML
    private ComboBox<Integer> user_id_dropdown;

    @FXML
    private Button app_save_button;

    @FXML
    private Button app_cancel_button;

    private int applID;

    /**
     * Constructor
     */
    public modifyAppointment() {
    }


    /**
     * recieves information from the selected appointment
     * @param appointment
     */
    public void sendAppointment(Appointment appointment) {
        this.app_app_ID.setText(String.valueOf(appointment.getAppointment_ID()));
        this.app_title_field.setText(String.valueOf(appointment.getTitle()));
        this.app_desc_field.setText(String.valueOf(appointment.getDescription()));
        this.app_location_field.setText(String.valueOf(appointment.getLocation()));
        this.app_type_field.setText(String.valueOf(appointment.getType()));
        this.app_date_field.setValue(LocalDate.from(appointment.getStart()));
        this.app_start_hour.setValue(Integer.valueOf(appointment.getStart().getHour()));
        this.app_start_min.setValue(Integer.valueOf(appointment.getStart().getMinute()));
        this.app_end_date.setValue(LocalDate.from(appointment.getEnd()));
        this.app_end_hour.setValue(Integer.valueOf(appointment.getEnd().getHour()));
        this.app_end_min.setValue(Integer.valueOf(appointment.getEnd().getMinute()));
        this.customer_ID_dropdown.setValue(Integer.valueOf(appointment.getCustomer_ID()));
        this.contact_id_dropdown.setValue(Integer.valueOf(appointment.getContact()));
        this.user_id_dropdown.setValue(Integer.valueOf(appointment.getUser_ID()));
    }

    /**
     * cancels the modify appointment
     * @param event
     * @throws IOException
     */
    @FXML
    void cancel_action(ActionEvent event)  throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/mainScreen.fxml"));
        loader.load();
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();
    }

    /**
     * saves the modified appointment
     * @param event
     * @throws IOException
     */
    @FXML
    void save_action(ActionEvent event)  throws IOException{
        Alert alert;

        this.applID = Integer.parseInt(this.app_app_ID.getText());

        if(!this.app_title_field.getText().isEmpty() && !this.app_desc_field.getText().isEmpty() && !this.app_location_field.getText().isEmpty() && !this.app_type_field.getText().isEmpty() && this.app_date_field.getValue() != null && this.app_end_date.getValue() != null && this.app_start_hour.getValue() != null && this.app_start_min.getValue() != null && this.app_end_hour.getValue() != null && this.app_end_min.getValue() != null && this.customer_ID_dropdown.getValue() != null && this.contact_id_dropdown.getValue() != null && this.user_id_dropdown.getValue() != null) {

            LocalDate startDate = LocalDate.parse(app_date_field.getValue().toString());
            LocalDate endDate = LocalDate.parse(app_end_date.getValue().toString());

            LocalTime startTimer = LocalTime.of(app_start_hour.getValue(), app_start_min.getValue());
            LocalTime endTimer = LocalTime.of(app_end_hour.getValue(), app_end_min.getValue());
            ZonedDateTime startTime = LocalDateTime.of(startDate, startTimer).atZone(localZone);
            ZonedDateTime endTime = LocalDateTime.of(endDate, endTimer).atZone(localZone);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(UTC);
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formatStart = startTime.format(formatter);
            String formatEnd = endTime.format(formatter);

            LocalDate todayDay = LocalDate.now();

            ZonedDateTime openingHours = LocalDateTime.of(todayDay, startTimer).atZone(localZone);
            ZonedDateTime closingHours = LocalDateTime.of(todayDay, endTimer).atZone(localZone);

            LocalDateTime now = LocalDateTime.now();
            String today = now.format(formatter);


            String appTitle = this.app_title_field.getText();
            String appDesc = this.app_desc_field.getText();
            String appLoc = this.app_location_field.getText();
            Integer appCont = this.contact_id_dropdown.getValue();
            String appType = this.app_type_field.getText();
            Integer appCust = this.customer_ID_dropdown.getValue();
            Integer appUser = this.user_id_dropdown.getValue();

            if(startTime.isAfter(endTime)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Start time must be before end time.");
                alert.showAndWait();
            }
            else {
                if (duringOfficeHours(openingHours) && duringOfficeHours(closingHours)) {
                    if(Application.customerID_Appointments.containsValue(applID)){
                        Application.customerID_Appointments.replace(applID,appCust);
                    }
                    if(overlappingApp(appCust,startTime,endTime, applID)) {
                        Application.updateAppointment(MainScreenController.index, new Appointment(this.applID, this.app_title_field.getText(), this.app_desc_field.getText(), this.app_location_field.getText(), this.contact_id_dropdown.getValue(), this.app_type_field.getText(), startTime, endTime, this.customer_ID_dropdown.getValue(), this.user_id_dropdown.getValue()));
                        try {
                            Connection myConn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ05k6r", "U05k6r", "53688529325");
                            Statement myStmt = myConn.createStatement();
                            myStmt.execute("UPDATE appointments SET Appointment_ID ='" + applID + "',Title = '" + appTitle + "',Description = '" + appDesc + "',Location = '" + appLoc + "',Type = '" + appType + "',Start = STR_TO_DATE('" + formatStart + "','%d/%m/%Y %H:%i:%s'),End = STR_TO_DATE('" + formatEnd + "','%d/%m/%Y %H:%i:%s'),Last_Update = now(),Last_Updated_By = '" + username + "',Customer_ID = '" + appCust + "',User_ID = '" + appUser + "',Contact_ID = '" + appCont + "' WHERE Appointment_ID ='" + applID + "'");
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(this.getClass().getResource("/view/mainScreen.fxml"));
                        loader.load();
                        this.stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        Parent scene = (Parent) loader.getRoot();
                        this.stage.setScene(new Scene(scene));
                        this.stage.show();
                    }
                    else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Appointments must not overlap existing appointments.");
                        alert.showAndWait();
                    }
                }
                else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Appointments must be between 8:00am and 10:00pm EST");
                    alert.showAndWait();
                }
            }


        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Missing Information");
            alert.setContentText("Please fill in all the blanks.");
            alert.showAndWait();
        }

    }

    /**
     * Initializes the modify appointment controller
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {

        app_start_hour.getItems().removeAll(app_start_hour.getItems());
        app_start_hour.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24);
        //app_start_hour.getSelectionModel().select(1);

        app_end_hour.getItems().removeAll(app_end_hour.getItems());
        app_end_hour.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24);
        // app_end_hour.getSelectionModel().select(1);

        app_start_min.getItems().removeAll(app_start_min.getItems());
        app_start_min.getItems().addAll(0,15,30,45);
        //app_start_min.getSelectionModel().select(0);

        app_end_min.getItems().removeAll(app_start_min.getItems());
        app_end_min.getItems().addAll(0,15,30,45);
        //app_end_min.getSelectionModel().select(0);

        customer_ID_dropdown.getItems().removeAll(customer_ID_dropdown.getItems());
        customer_ID_dropdown.getItems().addAll(Application.customerID);

        contact_id_dropdown.getItems().removeAll(contact_id_dropdown.getItems());
        contact_id_dropdown.getItems().addAll(Application.contactID);

        user_id_dropdown.getItems().removeAll(user_id_dropdown.getItems());
        user_id_dropdown.getItems().addAll(Application.userID);

    }
}