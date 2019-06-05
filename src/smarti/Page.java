package smarti;

public enum Page {
    SIGN_UP("/pages/signUp.fxml"),
    DASHBOARD("/pages/dashboard.fxml"),
    ADMIN_PANEL("/pages/adminPanel.fxml");

    private String page;

    Page(String value) {
        this.page = value;
    }

    public String getPage() {
        return page;
    }
}
