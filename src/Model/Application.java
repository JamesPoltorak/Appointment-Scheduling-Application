package Model;

import Controller.LoginController;
import Controller.Reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;
import java.util.stream.Collectors;

import static Controller.LoginController.username;

/**
 * Application class governs the whole application
 * with various functions
 */
public class Application {

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static ArrayList<Integer> customerID = new ArrayList<Integer>(100);
    public static ArrayList<Integer> contactID = new ArrayList<Integer>(100);
    public static ArrayList<Integer> userID = new ArrayList<Integer>(100);
    public static ArrayList<Integer> appointmentID = new ArrayList<Integer>(100);
    public static ArrayList<String> countryUS = new ArrayList<>();
    public static ArrayList<String> countryUK = new ArrayList<>();
    public static ArrayList<String> countryCAN = new ArrayList<>();

    public static int customerInc;
    public static int appointmentInc;

    public static HashMap<String, Integer> UKCountries = new HashMap<String, Integer>();
    public static HashMap<String, Integer> CANCountries = new HashMap<String, Integer>();
    public static HashMap<String, Integer> USCountries = new HashMap<String, Integer>();
    public static Map<String, ArrayList<String>> data = new HashMap<>();

    public static HashMap<Integer, Integer> customerID_Appointments = new HashMap<>();

    public static HashMap<String, Integer> user_ID = new HashMap<>();


    /**
     * constructor
     */
    public Application() {

    }

    /**
     * Adds a customer to the list
     * @param newCustomer
     */
    public static void addCustomer(Customer newCustomer) {
        allCustomers.add(newCustomer);
    }

    /**
     * Adds an appointment to the list
     * @param newAppointment
     */
    public static void addAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }

    /**
     * removes an existing customer
     * @param existingCustomer
     */
    public static void deleteCustomer(Customer existingCustomer) { allCustomers.remove(existingCustomer);}

    /**
     * removes an existing appointment
     * @param existingAppointment
     */
    public static void deleteAppointment(Appointment existingAppointment) {allAppointments.remove(existingAppointment);}

    /**
     * returns all appointments
     * @return
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    /**
     * returns all customers
     * @return
     */
    public static ObservableList<Customer> getAllCustomers() {
        return  allCustomers;
    }

    /**
     * keeps the count of customer ID's
     * @return
     */
    public static int customerIDCount() {
        ++customerInc;
        return customerInc;
    }

    /**
     * keeps the count of the appointment ID's
     * @return
     */
    public static int appointmentIDCount() {
        ++appointmentInc;
        return appointmentInc;
    }

    public static ZoneId localZone = ZoneId.systemDefault();

    /**
     * checks to see if a date time is during office hours
     * @param dateTime
     * @return
     */
    public static boolean duringOfficeHours(ZonedDateTime dateTime) {
        LocalTime officeStart = LocalTime.of(8,00);
        LocalTime officeEnd = LocalTime.of(22, 00);
        LocalDate today = LocalDate.now();
        ZonedDateTime officeOpen = ZonedDateTime.of(today,officeStart, ZoneId.of("America/New_York"));
        ZonedDateTime officeClose = ZonedDateTime.of(today,officeEnd,ZoneId.of("America/New_York"));

        if((dateTime.isAfter(officeOpen) && dateTime.isBefore(officeClose)) || dateTime.isEqual(officeOpen)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * checks to see if the appointment overlaps with the same customers other appointments
     * @param customerID
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean overlappingApp(Integer customerID, ZonedDateTime startTime, ZonedDateTime endTime, Integer appointmentID){
        for(Customer customers : allCustomers){
            if(customerID == customers.getCustomer_ID()){
                for (Appointment appointment : allAppointments){
                    if(customerID == appointment.getCustomer_ID()){
                        if(appointmentID != appointment.getAppointment_ID()) {
                            if ((startTime.isAfter(appointment.getStart()) && startTime.isBefore(appointment.getEnd())) ||
                                    (endTime.isAfter(appointment.getStart()) && endTime.isBefore(appointment.getEnd())) ||
                                    (startTime.isBefore(appointment.getStart()) && endTime.isAfter(appointment.getEnd())) ||
                                    (startTime.isEqual(appointment.getStart()) && endTime.isEqual(appointment.getEnd()))) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks to see if there is an upcoming appointment and alerts the user
     */
    public static void upcomingAppointment(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        for (Appointment appointment : allAppointments){
            int currentUserID = user_ID.get(username);
            if(appointment.getUser_ID() == currentUserID){
                if(appointment.getStart().isBefore(ZonedDateTime.now().plusMinutes(15)) && appointment.getStart().isAfter(ZonedDateTime.now())){
                    Alert alert;
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upcoming Appointment");
                    alert.setContentText("Appointment ID: " + appointment.getAppointment_ID() + "\r\n" + "Appointment Start: " + appointment.getStart().format(formatter));
                    alert.showAndWait();
                    return;
                }
            }
        }
        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcoming Appointment");
        alert.setContentText("There are no appointments in the next 15 minutes for user " + username);
        alert.showAndWait();
        return;
    }

    public static Locale frenchLocale = new Locale("fr_fr");
    public static Locale defaultLocale = new Locale(Locale.getDefault().toString());

    /**
     * checks to see if the locale is french
     * @param locale1
     * @return
     */
    public static boolean isFrench(Locale locale1) {
        if(locale1.equals(frenchLocale)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * wrong info error
     */
    public static void wrongInfo(){
        Alert alert;

        if(isFrench(defaultLocale)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Les informations sont incorrectes.");
            alert.showAndWait();
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Information is incorrect");
            alert.showAndWait();
        }
    }

    /**
     * missing info error
     */
    public static void missingInfo(){
        Alert alert;

        if(isFrench(defaultLocale)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please fill in all blanks");
            alert.showAndWait();
        }
    }

    /**
     * updates an appointment
     * @param index
     * @param selectedAppointment
     */
    public static void updateAppointment(int index, Appointment selectedAppointment) {allAppointments.set(index, selectedAppointment);}

    /**
     * updates a customer
     * @param index
     * @param selectedCustomer
     */
    public static void updateCustomer(int index, Customer selectedCustomer) {allCustomers.set(index, selectedCustomer);}

    /**
     * starts the application with querying the database
     */
    public static void startApplication() {
        try {
            Connection myConn = DriverManager.getConnection("jdbc:mysql://wgudb.ucertify.com:3306/WJ05k6r", "U05k6r", "53688529325");
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from appointments");
            while (myRs.next()) {
                addAppointment(new Appointment(myRs.getInt("Appointment_ID"), myRs.getString("Title"), myRs.getString("Description"), myRs.getString("Location"),myRs.getInt("Contact_ID"), myRs.getString("Type"), myRs.getTimestamp("Start").toLocalDateTime().atZone(localZone), myRs.getTimestamp("End").toLocalDateTime().atZone(localZone), myRs.getInt("Customer_ID"), myRs.getInt("User_ID")));
                appointmentID.add(myRs.getInt("Appointment_ID"));
                customerID_Appointments.put(myRs.getInt("Appointment_ID"), myRs.getInt("Customer_ID"));

            }
            Statement myStmt2 = myConn.createStatement();
            ResultSet myRs2 = myStmt2.executeQuery("select * from customers");
            while (myRs2.next()) {
                addCustomer(new Customer(myRs2.getInt("Customer_ID"), myRs2.getString("Customer_Name"), myRs2.getString("Address"), myRs2.getInt("Postal_Code"), myRs2.getLong("Phone"), myRs2.getInt("Division_ID")));
                customerID.add(myRs2.getInt("Customer_ID"));
            }
            Statement myStmt3 = myConn.createStatement();
            ResultSet myRs3 = myStmt3.executeQuery("select * from contacts");
            while (myRs3.next()) {
                contactID.add(myRs3.getInt("Contact_ID"));
            }
            Statement myStmt4 = myConn.createStatement();
            ResultSet myRs4 = myStmt4.executeQuery("select * from users");
            while (myRs4.next()) {
                userID.add(myRs4.getInt("User_ID"));
                user_ID.put(myRs4.getString("User_Name"), myRs4.getInt("User_ID"));
            }
            Statement myStmt6 = myConn.createStatement();
            ResultSet myRs6 = myStmt6.executeQuery("select * from first_level_divisions WHERE COUNTRY_ID = 38");
            while (myRs6.next()) {
                countryCAN.add(myRs6.getString("Division"));
                CANCountries.put(myRs6.getString("Division"), myRs6.getInt("Division_ID"));
            }
            Statement myStmt7 = myConn.createStatement();
            ResultSet myRs7 = myStmt7.executeQuery("select * from first_level_divisions WHERE COUNTRY_ID = 230");
            while (myRs7.next()) {
                countryUK.add(myRs7.getString("Division"));
                UKCountries.put(myRs7.getString("Division"), myRs7.getInt("Division_ID"));
            }
            Statement myStmt8 = myConn.createStatement();
            ResultSet myRs8 = myStmt8.executeQuery("select * from first_level_divisions WHERE COUNTRY_ID = 231");
            while (myRs8.next()) {
                countryUS.add(myRs8.getString("Division"));
                USCountries.put(myRs8.getString("Division"), myRs8.getInt("Division_ID"));
            }

        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        customerInc = Application.customerID.get(customerID.size()-1) + 1;
        appointmentInc = Application.appointmentID.get(appointmentID.size()-1) + 1;
    }
}
