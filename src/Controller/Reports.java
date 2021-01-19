package Controller;

import Model.Application;
import Model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static Model.Application.allAppointments;

/**
 * Controls the reports screen
 */
public class Reports {
    Stage stage;

    @FXML
    private Button customer_type;

    @FXML
    private Button contact_schedule;

    @FXML
    private Button appointments_location;

    @FXML
    private Button customer_month;

    @FXML
    private Button back;

    @FXML
    private TitledPane report_window;

    @FXML
    private Label report_field;


    /**
     * Creates a report that shows appointments by location
     * @param event
     */
    @FXML
    void appointments_location_click(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        Map<String, Long> appointmentsMonth = allAppointments.stream().collect(Collectors.groupingBy(Appointment::getLocation, Collectors.counting()));
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Appointments by Location.txt"), "utf-8"))) {
            for(Map.Entry<String, Long> entry : appointmentsMonth.entrySet()) {
                sb.append("\r\n" + "Location: "+entry.getKey()+ ", Amount of Appointments: " +entry.getValue());
                writer.write("Location: "+entry.getKey()+ ", Amount of Appointments: " +entry.getValue());
                writer.write(System.getProperty("line.separator"));
            }
            report_field.setText(sb.toString());
        }
        catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    /**
     * cancels the report screen
     * @param event
     * @throws IOException
     */
    @FXML
    void back_click(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/mainScreen.fxml"));
        loader.load();
        this.stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = (Parent)loader.getRoot();
        this.stage.setScene(new Scene(scene));
        this.stage.show();
    }

    /**
     * Creates a report that shows a schedule by contact
     * @param event
     */
    @FXML
    void contact_schedule_click(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        List<Appointment> sortedList = allAppointments.stream().sorted(Comparator.comparingInt(Appointment::getContact)).collect(Collectors.toList());
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Contact Schedule.txt"), "utf-8"))) {
            for(Appointment appointment : sortedList){
                sb.append("\r\n" + "Contact ID: "+appointment.getContact() +"\r\n" + "Appointment ID: " +appointment.getAppointment_ID() +"\r\n" + "Title: "+appointment.getTitle() +"\r\n" +"Type: "+appointment.getType() +"\r\n"+"Description: "+appointment.getDescription() +"\r\n"+ "Start Time: "+appointment.getStart().format(formatter) +"\r\n"+ "End Time: "+appointment.getEnd().format(formatter) +"\r\n"+ "Customer ID: "+appointment.getCustomer_ID() +"\r\n"+"\r\n");
                writer.write("Contact ID: "+appointment.getContact());
                writer.write(System.getProperty("line.separator"));
                writer.write("Appointment ID: " +appointment.getAppointment_ID());
                writer.write(System.getProperty("line.separator"));
                writer.write("Title: "+appointment.getTitle());
                writer.write(System.getProperty("line.separator"));
                writer.write("Type: "+appointment.getType());
                writer.write(System.getProperty("line.separator"));
                writer.write("Description: "+appointment.getDescription());
                writer.write(System.getProperty("line.separator"));
                writer.write("Start Time: "+appointment.getStart().format(formatter));
                writer.write(System.getProperty("line.separator"));
                writer.write("End Time: "+appointment.getEnd().format(formatter));
                writer.write(System.getProperty("line.separator"));
                writer.write("Customer ID: "+appointment.getCustomer_ID());
                writer.write(System.getProperty("line.separator"));
                writer.write(System.getProperty("line.separator"));
            }
            report_field.setText(sb.toString());
        }
        catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Creates a report that shows the amount of customer appointments by month
     * @param event
     */
    @FXML
    void customer_month_click(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        Map<Month, Long> appointmentsMonth = allAppointments.stream().collect(Collectors.groupingBy(Appointment::getMonths, Collectors.counting()));
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Appointments by Month.txt"), "utf-8"))) {
            for(Map.Entry<Month, Long> entry : appointmentsMonth.entrySet()) {
                sb.append("\r\n" + "Month: "+entry.getKey()+ ", Amount of Appointments: " +entry.getValue());
                writer.write("Month: "+entry.getKey()+ ", Amount of Appointments: " +entry.getValue());
                writer.write(System.getProperty("line.separator"));
            }
            report_field.setText(sb.toString());
        }
        catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Creates a report that shows the amount of appointments by type
     * @param event
     */
    @FXML
    void customer_type_click(ActionEvent event) {
        StringBuilder sb = new StringBuilder();
        Map<String, Long> appointmentsType = allAppointments.stream().collect(Collectors.groupingBy(Appointment::getType, Collectors.counting()));
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Appointments by Type.txt"), "utf-8"))) {
            for(Map.Entry<String, Long> entry : appointmentsType.entrySet()) {
                sb.append("\r\n" + "Type: "+entry.getKey()+ ", Amount of Appointments: " +entry.getValue() + "\r\n");
                writer.write("Type: "+entry.getKey()+ ", Amount of Appointments: " +entry.getValue());
                writer.write(System.getProperty("line.separator"));
            }
            report_field.setText(sb.toString());
        }
        catch (IOException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }
}
