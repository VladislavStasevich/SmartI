package smarti;

import javafx.application.Application;
import javafx.stage.Stage;
import view.Page;
import view.Screen;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Screen.initStage(primaryStage);
        Screen.switchTo(Page.SIGN_UP);
        Screen.setTitle("SmartI");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
