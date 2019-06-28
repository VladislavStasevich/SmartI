package smarti;

public enum Page {
    ADD_NEW_DEVICE("/pages/addNewCar.fxml"),
    SIGN_UP("/pages/signUp.fxml"),
    DASHBOARD("/pages/dashboard.fxml");

    private String page;

    Page(String value) {
        this.page = value;
    }

    public String getPage() {
        return page;
    }
}
