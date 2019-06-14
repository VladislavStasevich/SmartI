package controllers;

import dao.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import store.Database;
import store.Page;
import store.Store;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNewItemController implements Initializable {

    @FXML
    TextField name;

    @FXML
    TextField type;

    @FXML
    TextField count;

    @FXML
    TextField price;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onSubmit() throws SQLException {
        Database.addNewItem(new Item(name.getText(), type.getText(), Double.parseDouble(count.getText()),
                Double.parseDouble(price.getText())));
        Store.switchTo(Page.DASHBOARD);
    }

    public void onCancel() {
        Store.switchTo(Page.DASHBOARD);
    }

    public void onPriceKeyTyped(KeyEvent event) {
        Store.consumeIfNotValidNumber(price, event);
    }

    public void onCountKeyTyped(KeyEvent event) {
        Store.consumeIfNotValidNumber(count, event);
    }
}
