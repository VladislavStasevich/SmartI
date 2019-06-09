package controllers;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import dao.TableEmployee;
import javafx.scene.text.Text;
import smarti.Context;
import smarti.Database;
import smarti.Page;
import smarti.Screen;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private Label name;

    @FXML
    private Tab catalog;

    @FXML
    private ComboBox catalogManufacturer;

    @FXML
    private ComboBox catalogModel;

    @FXML
    private Tab checklist;

    @FXML
    private Tab employees;

    @FXML
    private TableView employeeTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMainPanel();
        initCatalogTab();
        initEmployeeTab();
    }

    private void initMainPanel() {
        try {
            byte[] file = Database.getFileByUserId(Context.currentEmployee.getId());
            if (file != null) {
                Image i = new Image(new ByteInputStream(file, file.length));
                this.imageView.setImage(i);
                name.setText(Context.currentEmployee.toFirstAndLastName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEmployeeTab() {
        if (Context.currentEmployee.getRole() == 0) {
            employees.setDisable(false);
        }
    }

    private void initCatalogTab() {
        catalogManufacturer.setPromptText("Test");
        catalogManufacturer.getItems().addAll(new Text("Test"), new Text("Test 2"));
    }

    public void logout(ActionEvent e) {
        Context.currentEmployee = null;
        Screen.switchTo(Page.SIGN_UP);
    }

    public void onEmployeesChanged(Event e) {
        try {
            employeeTable.getItems().clear();

            employeeTable.getColumns().forEach((col) ->
                    ((TableColumn)col).setCellValueFactory(new PropertyValueFactory<>(((TableColumn)col).getId())));
            List<TableEmployee> employees = Database.getTableEmployee();
            employees.forEach(te -> {
                ImageView iv = new ImageView();
                if (te.getPicture() != null) {
                    iv.setImage(new Image(new ByteInputStream(te.getPicture(), te.getPicture().length)));
                } else {
                    iv.setFitHeight(32);
                }
                employeeTable.getItems().add(new models.TableEmployee(
                        iv,
                        te.getFirstName(),
                        te.getMiddleName(),
                        te.getLastName(),
                        te.getRole())
                );
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onAddNewDevice(Event e) {
        Screen.switchTo(Page.ADD_NEW_DEVICE);
    }
}
