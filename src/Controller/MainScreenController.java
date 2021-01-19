package Controller;

import Model.Application;
import Model.Appointment;
import Model.Customer;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Filter;

import static Model.Application.*;
import static java.time.DayOfWeek.*;
import static java.time.temporal.TemporalAdjusters.*;

/**
 * Controls the main screen
 */
public class MainScreenController implements Initializable {


    Stage stage;
    Parent scene;
    public static int index;

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> table_app_ID;

    @FXML
    private TableColumn<Appointment, String> table_app_title;

    @FXML
    private TableColumn<Appointment, String> table_app_desc;

    @FXML
    private TableColumn<Appointment, String> table_app_location;

    @FXML
    private TableColumn<Appointment, Integer> table_app_contact;

    @FXML
    private TableColumn<Appointment, String> table_app_type;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> table_app_start;

    @FXML
    private TableColumn<Appointment, ZonedDateTime> table_app_end;

    @FXML
    private TableColumn<Appointment, String> table_app_customer_ID;

    @FXML
    private Button add_app_button;

    @FXML
    private Button modify_app_button;

    @FXML
    private Button delete_app_button;

    @FXML
    private RadioButton all_app_radio;

    @FXML
    private RadioButton month_app_radio;

    @FXML
    private RadioButton week_app_radio;

    @FXML
    private TableView<Customer> customersTable;

    @FXML
    private TableColumn<Customer, Integer> table_cust_ID;

    @FXML
    private TableColumn<Customer, String> table_cust_name;

    @FXML
    private TableColumn<Customer, String> table_cust_address;

    @FXML
    private TableColumn<Customer, Integer> table_cust_post;

    @FXML
    private TableColumn<Customer, Long> table_cust_number;

    @FXML
    private TableColumn<Customer, Integer> Division_ID;

    @FXML
    private Button add_customer_button;

    @FXML
    private Button modify_customer_button;

    @FXML
    private Button delete_customer_button;

    @FXML
    private Button generate_reports_button;

    @FXML
    private Button logout_button;

    /**
     * Add appointment
     * @param event
     * @throws IOException
     */
    @FXML
    void add_app_click(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/addAppointment.fxml"));
        loader.load();
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();

    }

    /**
     * Add customers
     * @param event
     * @throws IOException
     */
    @FXML
    void add_customer_click(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/addCustomer.fxml"));
        loader.load();
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();

    }

    /**
     * Deletes selected appointment
     * @param event
     */
    @FXML
    void delete_app_click(ActionEvent event) {
        Appointment selectedAppointment = (Appointment)this.appointmentsTable.getSelectionModel().getSelectedItem();
        int appointmentID = selectedAppointment.getAppointment_ID();
        int custoemrID = selectedAppointment.getCustomer_ID();
        String type = selectedAppointment.getType();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment?");
        alert.setHeaderText("Delete Appointment number "+appointmentID+" type: " +type+"?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Application.deleteAppointment(selectedAppointment);
            customerID_Appointments.remove(appointmentID,custoemrID);

            try {
                Connection myConn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ05k6r", "U05k6r", "53688529325");
                Statement myStmt = myConn.createStatement();
                myStmt.execute("DELETE from appointments WHERE Appointment_ID ='"+appointmentID+"'");
            }
            catch (Exception exc) {
                exc.printStackTrace();
            }

        }
    }

    /**
     * Deletes selected customer
     * @param event
     */
    @FXML
    void delete_customer_click(ActionEvent event) {
        Customer selectedCustomer = (Customer)this.customersTable.getSelectionModel().getSelectedItem();
        int customerID = selectedCustomer.getCustomer_ID();
        String customerName = selectedCustomer.getCustomer_Name();

        if(customerID_Appointments.containsValue(customerID)){
            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please delete all associated appointments;");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete " + customerName + "?");
            alert.setHeaderText("Delete "+ customerName +"?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Application.deleteCustomer(selectedCustomer);
                Application.customerID.remove(Integer.valueOf(customerID));

                try {
                    Connection myConn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ05k6r", "U05k6r", "53688529325");
                    Statement myStmt = myConn.createStatement();
                    myStmt.execute("DELETE from customers WHERE Customer_ID ='"+customerID+"'");
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }

            }
        }

    }

    /**
     * Opens the generate reports screen
     * @param event
     * @throws IOException
     */
    @FXML
    void generate_reports_click(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/reports.fxml"));
        loader.load();
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();
    }

    /**
     * logs out of the main screen
     * @param event
     * @throws IOException
     */
    @FXML
    void logout_click(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/login.fxml"));
        loader.load();
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();

    }

    /**
     * Modify selected appointment
     * @param event
     * @throws IOException
     */
    @FXML
    void modify_app_click(ActionEvent event)  throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/modifyAppointment.fxml"));
        loader.load();
        modifyAppointment modifyAppointmentController = (modifyAppointment)loader.getController();
        modifyAppointmentController.sendAppointment((Appointment)this.appointmentsTable.getSelectionModel().getSelectedItem());
        index = Application.getAllAppointments().indexOf(this.appointmentsTable.getSelectionModel().getSelectedItem());
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();
    }

    /**
     * modify selected customer
     * @param event
     * @throws IOException
     */
    @FXML
    void modify_customer_click(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/modifyCustomer.fxml"));
        loader.load();
        modifyCustomer modifyCustomerController = (modifyCustomer)loader.getController();
        modifyCustomerController.sendCustomer((Customer)this.customersTable.getSelectionModel().getSelectedItem());
        index = Application.getAllCustomers().indexOf(this.customersTable.getSelectionModel().getSelectedItem());
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();
    }

    /**
     * changes appointment view to all appointments
     * @param event
     */
    @FXML
    void all_appointments(ActionEvent event) {
        this.all_app_radio.setSelected(true);
        this.month_app_radio.setSelected(false);
        this.week_app_radio.setSelected(false);

        this.appointmentsTable.setItems(Application.getAllAppointments());
    }

    /**
     * changes appointment view to this month appointments
     * a lambda expression is used to here to sort the appointments by month.
     * @param event
     */
    @FXML
    void month_appointments(ActionEvent event) {
        this.all_app_radio.setSelected(false);
        this.month_app_radio.setSelected(true);
        this.week_app_radio.setSelected(false);

        final ZonedDateTime startMonth = LocalDateTime.now().with(firstDayOfMonth()).atZone(localZone);
        final ZonedDateTime endMonth = LocalDateTime.now().with(lastDayOfMonth()).atZone(localZone);

        FilteredList<Appointment> filteredItems = new FilteredList<>(getAllAppointments());

        filteredItems.setPredicate((appointment -> appointment.getStart().isBefore(endMonth) && appointment.getStart().isAfter(startMonth)));

        this.appointmentsTable.setItems(filteredItems);

    }

    /**
     * changes appointment view to this week appointments
     * a lambda expression is used to here to sort the appointments by week.
     * @param event
     */
    @FXML
    void week_appointments(ActionEvent event) {
        this.all_app_radio.setSelected(false);
        this.month_app_radio.setSelected(false);
        this.week_app_radio.setSelected(true);

        final ZonedDateTime lastweek = LocalDateTime.now().with(of(1)).atZone(localZone);
        final ZonedDateTime nextweek = LocalDateTime.now().with(of(7)).atZone(localZone);

        FilteredList<Appointment> filteredWeek = new FilteredList<>(getAllAppointments());

        filteredWeek.setPredicate((appointment -> appointment.getStart().isBefore(nextweek) && appointment.getStart().isAfter(lastweek)));


        this.appointmentsTable.setItems(filteredWeek);

    }

    /**
     * Initializes the main screen controller
     * a lambda expression was used here to dynamically change the format of the start date during initialization
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {


        this.all_app_radio.setSelected(true);

        this.appointmentsTable.setItems(Application.getAllAppointments());
        this.table_app_ID.setCellValueFactory(new PropertyValueFactory("Appointment_ID"));
        this.table_app_title.setCellValueFactory(new PropertyValueFactory("Title"));
        this.table_app_desc.setCellValueFactory(new PropertyValueFactory("Description"));
        this.table_app_location.setCellValueFactory(new PropertyValueFactory("Location"));
        this.table_app_ID.setCellValueFactory(new PropertyValueFactory("Appointment_ID"));
        this.table_app_contact.setCellValueFactory(new PropertyValueFactory("Contact"));
        this.table_app_type.setCellValueFactory(new PropertyValueFactory("Type"));
        this.table_app_start.setCellFactory(column -> {
            TableCell<Appointment, ZonedDateTime> cell = new TableCell<Appointment, ZonedDateTime>() {
                DateTimeFormatter outputformat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

                @Override
                protected void updateItem(ZonedDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        this.setText(outputformat.format(item));
                    }
                }
            };
            return cell;
        });
        this.table_app_start.setCellValueFactory(new PropertyValueFactory("Start"));
        this.table_app_end.setCellFactory(column -> {
            TableCell<Appointment, ZonedDateTime> cell = new TableCell<Appointment, ZonedDateTime>() {
                DateTimeFormatter outputformat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

                @Override
                protected void updateItem(ZonedDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        this.setText(outputformat.format(item));
                    }
                }
            };
            return cell;
        });
        this.table_app_end.setCellValueFactory(new PropertyValueFactory("End"));
        this.table_app_customer_ID.setCellValueFactory(new PropertyValueFactory("Customer_ID"));


        this.customersTable.setItems(Application.getAllCustomers());
        this.table_cust_ID.setCellValueFactory(new PropertyValueFactory("Customer_ID"));
        this.table_cust_name.setCellValueFactory(new PropertyValueFactory("Customer_Name"));
        this.table_cust_address.setCellValueFactory(new PropertyValueFactory("Address"));
        this.table_cust_post.setCellValueFactory(new PropertyValueFactory("Postal_Code"));
        this.table_cust_number.setCellValueFactory(new PropertyValueFactory("Phone"));
        this.Division_ID.setCellValueFactory(new PropertyValueFactory("Division_ID"));
    }

}
