package controllers;

import dao.NewCar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
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

public class AddNewCarController implements Initializable {

    private String imagePath;

    @FXML
    ImageView image;

    @FXML
    TextField manufacturer;

    @FXML
    TextField model;

    @FXML
    TextField engine;

    @FXML
    TextField transmission;

    @FXML
    DatePicker year;

    @FXML
    TextArea description;

    @FXML
    TextField price;

    private URL getBaseImageUrl() {
        return AddNewCarController.class.getResource("/addImage.png");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        String imagePath = getBaseImageUrl().toString();
        image.setImage(new Image(imagePath));
        this.imagePath = imagePath;
    }

    public void onImageViewMouseClick() {
        String imagePath = ImageFileChooser.getImageFilePath();
        if (!imagePath.equals("")) {
            image.setImage(new Image(imagePath));
            this.imagePath = imagePath;
        }
    }

    public void onSubmit() throws IOException, SQLException, URISyntaxException {
        byte[] fileContent = Files.readAllBytes(Paths.get(new URI(imagePath)));

        NewCar device = new NewCar(
                fileContent,
                manufacturer.getText(),
                model.getText(),
                engine.getText(),
                transmission.getText(),
                year.getValue().getYear(),
                description.getText(),
                Double.parseDouble(price.getText())
        );

        Database.addNewCar(device);

        Screen.switchTo(Page.DASHBOARD);
    }

    public void onCancel() {
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
