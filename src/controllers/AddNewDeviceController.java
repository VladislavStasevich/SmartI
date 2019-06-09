package controllers;

import dao.NewDevice;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import smarti.Database;
import smarti.Page;
import smarti.Screen;
import utils.ImageFileChooser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewDeviceController implements Initializable {

    private String imagePath;

    @FXML
    ImageView phoneImage;

    @FXML
    TextField manufacturer;

    @FXML
    TextField model;

    @FXML
    TextArea description;

    @FXML
    TextField price;

    private URL getBaseImageUrl() {
        return AddNewDeviceController.class.getResource("/addImage.png");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        String imagePath = getBaseImageUrl().toString();
        phoneImage.setImage(new Image(imagePath));
        this.imagePath = imagePath;
    }

    public void onImageViewMouseClick(Event e) {
        String imagePath = ImageFileChooser.getImageFilePath();
        phoneImage.setImage(new Image(imagePath));
        this.imagePath = imagePath;
    }

    public void onSubmit(Event e) throws IOException, SQLException, URISyntaxException {
        byte[] fileContent = Files.readAllBytes(Paths.get(new URI(imagePath)));

        NewDevice device = new NewDevice(
                fileContent,
                manufacturer.getText(),
                model.getText(),
                description.getText(),
                Double.parseDouble(price.getText())
        );

        Database.addNewDevice(device);

        Screen.switchTo(Page.DASHBOARD);
    }

    public void onPriceKeyTyped(KeyEvent ke) {
        if (!"1234567890.".contains(ke.getCharacter())) {
            ke.consume();
        }
        if (price.getText().contains(".") && ke.getCharacter().contentEquals(".")) {
            ke.consume();
        }
    }
}
