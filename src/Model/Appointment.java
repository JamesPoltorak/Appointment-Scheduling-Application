package Model;

import com.mysql.cj.conf.StringProperty;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static Model.Application.localZone;

/**
 * Creates the appointment class
 */
public class Appointment {
    private int Appointment_ID;
    private String Title;
    private String Description;
    private String Location;
    private int Contact;
    private String Type;
    private ZonedDateTime Start;
    private ZonedDateTime End;
    private int Customer_ID;
    private int User_ID;

    /**
     * Appointment constructor
     * @param Appointment_ID
     * @param Title
     * @param Description
     * @param Location
     * @param Contact
     * @param Type
     * @param Start
     * @param End
     * @param Customer_ID
     * @param User_ID
     */
    public Appointment(int Appointment_ID, String Title, String Description, String Location, int Contact, String Type, ZonedDateTime Start,ZonedDateTime End, int Customer_ID, int User_ID)
    {
        this.Appointment_ID = Appointment_ID;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.Contact = Contact;
        this.Type = Type;
        this.Start = Start;
        this.End = End;
        this.Customer_ID = Customer_ID;
        this.User_ID = User_ID;
    }

    /**
     * @return appointment ID
     */
    public int getAppointment_ID() {
        return Appointment_ID;
    }

    /**
     * set appointment ID
     * @param appointment_ID
     */
    public void setAppointment_ID(int appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * set title
     * @param title
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * set description
     * @param description
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return Location;
    }

    /**
     * set location
     * @param location
     */
    public void setLocation(String location) {
        Location = location;
    }

    /**
     * @return contact
     */
    public int getContact() {
        return Contact;
    }

    /**
     * set contact
     * @param contact
     */
    public void setContact(int contact) {
        Contact = contact;
    }

    /**
     * @return type
     */
    public String getType() {
        return Type;
    }

    /**
     * set type
     * @param type
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * @return start time
     */
    public ZonedDateTime getStart() {
        return Start;
    }

    /**
     * set start time
     * @param start
     */
    public void setStart(ZonedDateTime start) {
        Start = start;
    }

    /**
     * @return end time
     */
    public ZonedDateTime getEnd() {
        return End;
    }

    /**
     * set end time
     * @param end
     */
    public void setEnd(ZonedDateTime end) {
        End = end;
    }

    /**
     * @return customer ID
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /**
     * set customer ID
     * @param customer_ID
     */
    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     * @return user ID
     */
    public int getUser_ID() {return User_ID;}

    /**
     * set user ID
     * @param user_ID
     */
    public void setUser_ID(int user_ID) {User_ID = user_ID;}

    /**
     * @return month of start date
     */
    public Month getMonths(){
        return getStart().getMonth();
    }

}
