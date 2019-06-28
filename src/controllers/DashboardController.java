package controllers;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import dao.NewCar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import models.TableCheck;
import smarti.*;
import utils.Assert;
import utils.CheckPrinter;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;


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
    private ComboBox<String> catalogEngine;

    @FXML
    private ComboBox<String> catalogTransmission;

    @FXML
    private ComboBox<String> catalogYear;

    @FXML
    private ImageView catalogImage;

    @FXML
    private Label catalogDescription;

    @FXML
    private Label catalogPrice;

    @FXML
    private Tab employees;

    @FXML
    private TableView<models.TableCheck> checkListTable;

    @FXML
    private TableView<models.TableEmployee> employeeTable;

    @FXML
    private Button catalogAddNewDevice;

    @FXML
    private Label sessionTimer;

    @FXML
    private Label lastSessionTimer;

    public static SessionTimer session;

    @FXML
    private TextField catalogName;

    @FXML
    private TextField catalogLastName;

    @FXML
    private TextField catalogMiddleName;

    @FXML
    private TextField catalogPassport;

    @FXML
    private TextField catalogAddress;

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

    public void onLogout() {
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
        Set<String> passport = new HashSet<>();
        Database.getTableCheck().forEach(tableCheck -> {
            if (passport.contains(tableCheck.getPassport())) {
                return;
            }
            passport.add(tableCheck.getPassport());
            checkListTable.getItems().add(tableCheck);
        });
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
    }

    public void onModelAction() {
        String selectedManufacturer = this.catalogManufacturer.getValue();
        String selectedModel = this.catalogModel.getValue();
        List<String> engines = Database.getEnginesByManufacturerAndModel(selectedManufacturer, selectedModel);
        catalogEngine.getItems().clear();
        catalogEngine.getItems().addAll(engines);

        NewCar newCar = Database.getCarByManufacturerAndModel(selectedManufacturer, selectedModel);
        catalogDescription.setText(newCar.getDescription());
        catalogImage.setImage(new Image(new ByteInputStream(newCar.getImage(), newCar.getImage().length)));
        catalogPrice.setText(String.format("Цена: %.2f", newCar.getPrice()));
    }

    public void onEngineAction() {
        String selectedManufacturer = this.catalogManufacturer.getValue();
        String selectedModel = this.catalogModel.getValue();
        String selectedEngine = this.catalogEngine.getValue();
        List<String> transmissions = Database.getTransmissionByManufacturerAndModelAndEngine(selectedManufacturer, selectedModel, selectedEngine);
        catalogTransmission.getItems().clear();
        catalogTransmission.getItems().addAll(transmissions);
    }

    public void onTransmissionAction() {
        String selectedManufacturer = this.catalogManufacturer.getValue();
        String selectedModel = this.catalogModel.getValue();
        String selectedEngine = this.catalogEngine.getValue();
        String selectedTransmission = this.catalogTransmission.getValue();
        List<Integer> years = Database.getYearByManufacturerAndModelAndEngineAndTransmission(selectedManufacturer, selectedModel, selectedEngine, selectedTransmission);
        catalogYear.getItems().clear();
        catalogYear.getItems().addAll(years.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    public void onCheckListSave() {
        Window owner = imageView.getScene().getWindow();
        try {
            String lastName = Assert.assertStringNotEmpty(catalogName.getText(), "Фамилия");
            String firstName = Assert.assertStringNotEmpty(catalogLastName.getText(), "Имя");
            String middleName = Assert.assertStringNotEmpty(catalogMiddleName.getText(), "Отчество");
            String address = Assert.assertStringNotEmpty(catalogAddress.getText(), "Адрес");
            String passport = Assert.assertStringNotEmpty(catalogPassport.getText(), "Паспортные данные");
            String model = Assert.assertStringNotEmpty(catalogModel.getValue(), "Модель");
            String manufacturer = Assert.assertStringNotEmpty(catalogManufacturer.getValue(), "Производитель");
            String engine = Assert.assertStringNotEmpty(catalogEngine.getValue(), "Двигатель");
            String transmission = Assert.assertStringNotEmpty(catalogTransmission.getValue(), "Трансмиссия");
            String year = Assert.assertStringNotEmpty(catalogYear.getValue(), "Год выпуска");
            Database.addNewOffer(lastName, firstName, middleName, address, passport, model, manufacturer, engine, transmission, year);
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

    public void onCheckClick(MouseEvent event) {
        if (event.getButton().name().equals("SECONDARY")) {
            models.TableCheck check = checkListTable.getItems().get(checkListTable.getSelectionModel().getSelectedIndex());
            List<TableCheck> list = Database.getTableChecksForCustomer(check);
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html"));
            File file = fileChooser.showSaveDialog(imageView.getScene().getWindow());

            if (file != null) {
                CheckPrinter.print(file.getPath(), list);
            }
        }
    }
}
