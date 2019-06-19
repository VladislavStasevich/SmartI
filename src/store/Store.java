package store;

import controllers.DashboardController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Store extends Application {

    static private Stage applicationStage;

    @Override
    public void start(Stage primaryStage) {
        applicationStage = primaryStage;
        primaryStage.setResizable(false);
        switchTo(Page.SIGN_UP);
        applicationStage.setTitle("Поликлиника № 1337");
        primaryStage.setOnCloseRequest(event -> Platform.runLater(() -> {
            DashboardController.session.stop();
            System.exit(0);
        }));
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static void setStage(Stage stage, URL url) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(url)));
        centerWindow(stage);
        stage.show();
    }

    private static void centerWindow(Stage stage) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double centerX = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) * 1.0f / 2;
        double centerY = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) * 1.0f / 3;
        stage.setX(centerX);
        stage.setY(centerY);
    }

    public static void switchTo(Page page) {
        try {
            setStage(applicationStage, Store.class.getResource(page.getPage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
