package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import models.Employee;
import smarti.*;

public class SignUpController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Window owner = submitButton.getScene().getWindow();
        if(emailField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email");
            return;
        }
        if(passwordField.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
        }

        try {
            Employee e = Database.getEmployeeByLogin(emailField.getText());
            if (passwordField.getText().equals(e.getPassword())) {
                if (e.getRole() == EmployeeRole.ADMIN.getRole()) {
                    Screen.switchTo(Page.ADMIN_PANEL);
                } else {
                    Screen.switchTo(Page.DASHBOARD);
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
        }
    }
}