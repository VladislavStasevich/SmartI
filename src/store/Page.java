package store;

public enum Page {
    ADD_NEW_ITEM("/pages/addNewItem.fxml"),
    SIGN_UP("/pages/signUp.fxml"),
    MODIFYY_ITEM("/pages/modifyItem.fxml"),
    DASHBOARD("/pages/dashboard.fxml");

    private String page;

    Page(String value) {
        this.page = value;
    }

    public String getPage() {
        return page;
    }
}
