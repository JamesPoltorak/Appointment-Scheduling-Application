package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreenController {

    Stage stage;

    @FXML
    private TableColumn<?, ?> table_app_ID;

    @FXML
    private TableColumn<?, ?> table_app_title;

    @FXML
    private TableColumn<?, ?> table_app_desc;

    @FXML
    private TableColumn<?, ?> table_app_location;

    @FXML
    private TableColumn<?, ?> table_app_contact;

    @FXML
    private TableColumn<?, ?> table_app_type;

    @FXML
    private TableColumn<?, ?> table_app_start;

    @FXML
    private TableColumn<?, ?> table_app_end;

    @FXML
    private TableColumn<?, ?> table_app_customer_ID;

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
    private TableColumn<?, ?> table_cust_ID;

    @FXML
    private TableColumn<?, ?> table_cust_name;

    @FXML
    private TableColumn<?, ?> table_cust_address;

    @FXML
    private TableColumn<?, ?> table_cust_post;

    @FXML
    private TableColumn<?, ?> table_cust_number;

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

    @FXML
    void add_app_click(ActionEvent event) {

    }

    @FXML
    void add_customer_click(ActionEvent event) {

    }

    @FXML
    void delete_app_click(ActionEvent event) {

    }

    @FXML
    void delete_customer_click(ActionEvent event) {

    }

    @FXML
    void generate_reports_click(ActionEvent event) {

    }

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

    @FXML
    void modify_app_click(ActionEvent event) {

    }

    @FXML
    void modify_customer_click(ActionEvent event) {

    }

}
