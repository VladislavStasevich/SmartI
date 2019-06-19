package store;

import controllers.DashboardController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Store extends Application {

    static private Stage applicationStage;

    public static void consumeIfNotValidNumber(TextField element, KeyEvent event) {
        if (!"1234567890.".contains(event.getCharacter())) {
            event.consume();
        }
        if (element.getText().contains(".") && event.getCharacter().contentEquals(".")) {
            event.consume();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        applicationStage = primaryStage;
        primaryStage.setResizable(false);
        switchTo(Page.SIGN_UP);
        applicationStage.setTitle("Склад № 1");
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
        stage.show();
    }

    public static void switchTo(Page page) {
        try {
            setStage(applicationStage, Store.class.getResource(page.getPage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
