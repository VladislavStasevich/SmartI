package view;

public enum Page {
    SIGN_UP("smarti/sample.fxml");

    private String page;

    Page(String value) {
        this.page = value;
    }

    public String getPage() {
        return page;
    }
}
