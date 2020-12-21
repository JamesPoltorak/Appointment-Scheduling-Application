package Model;

import javafx.geometry.Pos;

public class Customer {
    private int Customer_ID;
    private String Customer_Name;
    private String Address;
    private int Postal_code;
    private int Phone;

    public Customer(int Customer_ID, String Customer_Name, String Address, int Postal_Code, int Phone)
    {
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
        this.Address = Address;
        this.Postal_code = Postal_Code;
        this.Phone = Phone;
    }

    public int getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getPostal_code() {
        return Postal_code;
    }

    public void setPostal_code(int postal_code) {
        Postal_code = postal_code;
    }

    public int getPhone() {
        return Phone;
    }

    public void setPhone(int phone) {
        Phone = phone;
    }
}
