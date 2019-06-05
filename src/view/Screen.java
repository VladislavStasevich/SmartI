package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        Parent root = FXMLLoader.load(url);
        stage.setScene(new Scene(root));
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
