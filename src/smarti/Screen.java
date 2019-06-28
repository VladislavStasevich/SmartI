package smarti;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Screen {
    static private Stage applicationStage;

    static void initStage(Stage stage) {
        applicationStage = stage;
        stage.setResizable(false);
        stage.getIcons().add(new Image(Screen.class.getResourceAsStream("/smarti.png")));
    }

    private static void setStage(Stage stage, URL url) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(url)));
        stage.show();
    }

    static void setTitle() {
        applicationStage.setTitle("Car catalog");
    }

    public static void switchTo(Page page) {
        try {
            setStage(applicationStage, Screen.class.getResource(page.getPage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
