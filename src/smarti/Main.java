package smarti;

import controllers.DashboardController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Screen.initStage(primaryStage);
        Screen.switchTo(Page.SIGN_UP);
        Screen.setTitle("SmartI");
        primaryStage.setOnCloseRequest(event -> Platform.runLater(() -> {
            DashboardController.session.stop();
            System.exit(0);
        }));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
