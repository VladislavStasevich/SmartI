package models;

public class TableItem {
    private String number;
    private String name;
    private String type;
    private String count;
    private String price;

    public TableItem(String number, String name, String type, String count, String price) {
        this.number = number;
        this.name = name;
        this.type = type;
        this.count = count;
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCount() {
        return count;
    }

    public String getPrice() {
        return price;
    }
}
