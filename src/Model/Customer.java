package Model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;

/**
 * Creates the customer class
 */
public class Customer {
    private int Customer_ID;
    private String Customer_Name;
    private String Address;
    private int Postal_Code;
    private long Phone;
    private int Division_ID;

    /**
     * Constructor
     * @param Customer_ID
     * @param Customer_Name
     * @param Address
     * @param Postal_Code
     * @param Phone
     * @param Division_ID
     */
    public Customer(int Customer_ID, String Customer_Name, String Address, int Postal_Code, long Phone, int Division_ID)
    {
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
        this.Address = Address;
        this.Postal_Code = Postal_Code;
        this.Phone = Phone;
        this.Division_ID = Division_ID;
    }

    /**
     * @return customer ID
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /**
     * sets customer ID
     * @param customer_ID
     */
    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     * @return Customer name
     */
    public String getCustomer_Name() {
        return Customer_Name;
    }

    /**
     * set customer name
     * @param customer_Name
     */
    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return Address;
    }

    /**
     * set address
     * @param address
     */
    public void setAddress(String address) {
        Address = address;
    }

    /**
     * @return postal code
     */
    public int getPostal_Code() {
        return Postal_Code;
    }

    /**
     * set postal code
     * @param postal_code
     */
    public void setPostal_Code(int postal_code) {
        Postal_Code = postal_code;
    }

    /**
     * @return phone number
     */
    public long getPhone() {
        return Phone;
    }

    /**
     * set phone number
     * @param phone
     */
    public void setPhone(long phone) {
        Phone = phone;
    }

    /**
     * @return division ID
     */
    public int getDivision_ID() {return Division_ID;}

    /**
     * set division ID
     * @param Division_ID
     */
    public void setDivision_ID(int Division_ID) {Division_ID = Division_ID;}
}
