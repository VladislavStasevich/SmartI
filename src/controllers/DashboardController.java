package controllers;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import dao.NewDevice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;
import smarti.*;
import utils.Assert;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private Label name;

    @FXML
    private ComboBox<String> catalogManufacturer;

    @FXML
    private ComboBox<String> catalogModel;

    @FXML
    private ImageView catalogImage;

    @FXML
    private Label catalogDescription;

    @FXML
    private Label catalogPrice;

    @FXML
    private TextField checkListLastName;

    @FXML
    private TextField checkListFirstName;

    @FXML
    private TextField checkListMiddleName;

    @FXML
    private TextField checkListAddress;

    @FXML
    private TextField checkListPassport;

    @FXML
    private TextField checkListModel;

    @FXML
    private TextField checkListManufacturer;

    @FXML
    private Tab employees;

    @FXML
    private TableView<models.TableCheck> checkListTable;

    @FXML
    private TableView<models.TableEmployee> employeeTable;

    @FXML
    private Button checkListSave;

    @FXML
    private Button catalogAddNewDevice;

    @FXML
    private Label sessionTimer;

    @FXML
    private Label lastSessionTimer;

    public static SessionTimer session;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMainPanel();
        initCatalogTab();
        initEmployeeTab();
        session = new SessionTimer(sessionTimer, lastSessionTimer);
        session.start();
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
            catalogAddNewDevice.setVisible(false);
        }
    }

    private void initEmployeeTab() {
        if (Context.currentEmployee.getRole() == 0) {
            employees.setDisable(false);
        }
    }

    private void initCatalogTab() {
        catalogManufacturer.setPromptText("Выберите производителя");
        catalogModel.setPromptText("Выберите модель");
        catalogManufacturer.getItems().addAll(Database.getManufacturers());
    }

    public void logout() {
        Context.currentEmployee = null;
        session.stop();
        Screen.switchTo(Page.SIGN_UP);
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
        Database.getTableCheck().forEach(tableCheck -> checkListTable.getItems().add(tableCheck));
    }

    public void onAddNewDevice() {
        Screen.switchTo(Page.ADD_NEW_DEVICE);
    }

    public void onManufacturerAction() {
        String selectedManufacturer = this.catalogManufacturer.getValue();
        List<String> models = Database.getModelsByManufacturer(selectedManufacturer);
        catalogImage.setImage(null);
        catalogDescription.setText("");
        catalogPrice.setText("");
        catalogModel.getItems().clear();
        catalogModel.getItems().addAll(models);
        checkListManufacturer.setText(selectedManufacturer);
    }

    public void onModelAction() {
        String selectedManufacturer = checkListManufacturer.getText();
        String selectedModel = this.catalogModel.getValue();
        checkListModel.setText(selectedModel);
        NewDevice newDevice = Database.getSmartphoneByManufacturerAndModel(selectedManufacturer, selectedModel);
        catalogDescription.setText(newDevice.getDescription());
        catalogImage.setImage(new Image(new ByteInputStream(newDevice.getImage(), newDevice.getImage().length)));
        catalogPrice.setText(String.format("Цена: %.2f", newDevice.getPrice()));
    }

    public void onCheckListSave() {
        Window owner = checkListSave.getScene().getWindow();
        try {
            String lastName = Assert.assertStringNotEmpty(checkListLastName.getText(), "Фамилия");
            String firstName = Assert.assertStringNotEmpty(checkListFirstName.getText(), "Имя");
            String middleName = Assert.assertStringNotEmpty(checkListMiddleName.getText(), "Отчество");
            String address = Assert.assertStringNotEmpty(checkListAddress.getText(), "Адрес");
            String passport = Assert.assertStringNotEmpty(checkListPassport.getText(), "Паспортные данные");
            String model = Assert.assertStringNotEmpty(checkListModel.getText(), "Модель");
            String manufacturer = Assert.assertStringNotEmpty(checkListManufacturer.getText(), "Производитель");
            Database.addNewOffer(lastName, firstName, middleName, address, passport, model, manufacturer);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (IllegalArgumentException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Ошибка ввода данных!",
                    String.format("Пожалуйста заполните поле '%s'", e.getMessage()));
            return;
        }
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Сохранено!", "Сохранено");
    }

    public void nameCheck(KeyEvent ke) {
        System.out.println(ke);
        if (!Character.isLetter(ke.getCharacter().charAt(0))) {
            ke.consume();
        }
    }

    public void passportCheck(KeyEvent ke) {
        if (!Character.isLetterOrDigit(ke.getCharacter().charAt(0)) || checkListPassport.getText().length() > 8) {
            ke.consume();
        }
    }
}
