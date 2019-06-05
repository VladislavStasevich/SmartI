package smarti;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Screen {
    static private Stage applicationStage;

    public static void initStage(Stage stage) {
        applicationStage = stage;
    }

    private static void setStage(Stage stage, URL url) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(url)));
        stage.show();
    }

    public static void setTitle(String title) {
        applicationStage.setTitle(title);
    }

    public static void switchTo(Page page) {
        try {
            setStage(applicationStage, Screen.class.getResource(page.getPage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
