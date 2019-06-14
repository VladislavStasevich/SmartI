package controllers;

import dao.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import store.Database;
import store.Page;
import store.Store;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewPatientController implements Initializable {

    @FXML
    TextField cardNumber;

    @FXML
    TextField firstName;

    @FXML
    TextField lastName;

    @FXML
    TextField middleName;

    @FXML
    TextField passport;

    @FXML
    TextField referral;

    @FXML
    TextField address;

    @FXML
    TextField record;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onSubmit() throws SQLException {
        Database.addNewPatient(new Patient(
                cardNumber.getText(),
                firstName.getText(),
                middleName.getText(),
                lastName.getText(),
                passport.getText(),
                referral.getText(),
                address.getText(),
                record.getText()
        ));
        Store.switchTo(Page.DASHBOARD);
    }

    public void onCancel() {
        Store.switchTo(Page.DASHBOARD);
    }
}
