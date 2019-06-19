package controllers;

import dao.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import models.TablePatient;
import store.Context;
import store.Database;
import store.Page;
import store.Store;
import utils.CheckPrinter;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyPatientController implements Initializable {

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
        TablePatient current = Context.currentPatient;
        cardNumber.setText(current.getCardNumber());
        firstName.setText(current.getFirstName());
        lastName.setText(current.getLastName());
        middleName.setText(current.getMiddleName());
        passport.setText(current.getPassport());
        referral.setText(current.getReferral());
        address.setText(current.getAddress());
        record.setText(current.getRecord());
    }

    public void onPrint() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html"));
        File file = fileChooser.showSaveDialog(cardNumber.getScene().getWindow());
        if (file != null) {
            CheckPrinter.print(file.getPath(), new Patient(
                    cardNumber.getText(),
                    firstName.getText(),
                    lastName.getText(),
                    middleName.getText(),
                    passport.getText(),
                    referral.getText(),
                    address.getText(),
                    record.getText()
            ));
        }
    }

    public void onClose() {
        Context.currentPatient = null;
        Store.switchTo(Page.DASHBOARD);
    }

    public void onSave() {
        try {
            Database.updatePatient(new Patient(
                    cardNumber.getText(),
                    firstName.getText(),
                    lastName.getText(),
                    middleName.getText(),
                    passport.getText(),
                    referral.getText(),
                    address.getText(),
                    record.getText()
            ), Context.currentPatient.getCardNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onClose();
    }

    public void onDelete() {
        Database.deletePatient(cardNumber.getText());
        onClose();
    }
}
