package controllers;

import dao.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import models.TableItem;
import store.Context;
import store.Database;
import store.Page;
import store.Store;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyItemController implements Initializable {

    @FXML
    Label number;

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
        TableItem current = Context.currentItem;

        number.setText(current.getNumber());
        name.setText(current.getName());
        type.setText(current.getType());
        count.setText(current.getCount());
        price.setText(current.getPrice());
    }

    public void onClose() {
        Context.currentItem = null;
        Store.switchTo(Page.DASHBOARD);
    }

    public void onSave() {
        try {
            Database.updateItem(new Item(name.getText(), type.getText(), Double.parseDouble(count.getText()),
                    Double.parseDouble(price.getText())), number.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onClose();
    }

    public void onDelete() {
        Database.deleteItem(number.getText());
        onClose();
    }

    public void onPriceKeyTyped(KeyEvent event) {
        Store.consumeIfNotValidNumber(price, event);
    }

    public void onCountKeyTyped(KeyEvent event) {
        Store.consumeIfNotValidNumber(count, event);
    }
}
