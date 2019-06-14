package controllers;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import models.TableItem;
import store.*;

import java.net.URL;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private Label name;

    @FXML
    private Tab employees;

    @FXML
    private TableView<TableItem> checkListTable;

    @FXML
    private TableView<models.TableEmployee> employeeTable;

    @FXML
    private Button catalogAddNewItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMainPanel();
        initEmployeeTab();
    }

    private void initMainPanel() {
        try {
            byte[] file = Database.getFileByUserId(Context.currentEmployee.getId());
            if (file != null) {
                Image i = new Image(new ByteInputStream(file, file.length));
                this.imageView.setImage(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText(Context.currentEmployee.toFirstAndLastName());
        if (Context.currentEmployee.getRole() != EmployeeRole.ADMIN.getRole()) {
            catalogAddNewItem.setVisible(false);
        }
    }

    private void initEmployeeTab() {
        if (Context.currentEmployee.getRole() == 0) {
            employees.setDisable(false);
        }
    }

    public void logout() {
        Context.currentEmployee = null;
        Store.switchTo(Page.SIGN_UP);
    }

    public void onEmployeesChanged() {
        employeeTable.getItems().clear();
        employeeTable.getColumns().forEach(col ->
                col.setCellValueFactory(new PropertyValueFactory<>((String)col.getProperties().get("name"))));
        Database.getTableEmployee().forEach(te -> {
            ImageView iv = new ImageView();
            if (te.getPicture() != null) {
                iv.setImage(new Image(new ByteInputStream(te.getPicture(), te.getPicture().length)));
            } else {
                iv.setFitHeight(32);
            }
            employeeTable.getItems().add(new models.TableEmployee(iv, te.getFirstName(), te.getMiddleName(),
                    te.getLastName(), te.getRole()));
        });
    }

    public void onCheckListChanged() {
        checkListTable.getItems().clear();
        checkListTable.getColumns().forEach(col ->
                col.setCellValueFactory(new PropertyValueFactory<>((String)col.getProperties().get("name"))));
        Database.getTableItems().forEach(tableItem -> checkListTable.getItems().add(tableItem));
    }

    public void onAddNewItem() {
        Store.switchTo(Page.ADD_NEW_ITEM);
    }

    public void onMouseClickingByRow() {
        Context.currentItem = checkListTable.getItems().get(checkListTable.getSelectionModel().getSelectedIndex());
        Store.switchTo(Page.MODIFYY_ITEM);
    }
}
