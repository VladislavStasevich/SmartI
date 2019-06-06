package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {
    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image i = new Image(new FileInputStream("E:\\smarti\\test.png"));
            this.imageView.setImage(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
