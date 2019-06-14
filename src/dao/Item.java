package dao;

public class Item {
    private String name;
    private String type;
    private double count;
    private double price;

    public Item(String name, String type, double count, double price) {
        this.name = name;
        this.type = type;
        this.count = count;
        this.price = price;

        if (name.equals("") || type.equals("") || count < 0 || price < 0) throw new IllegalArgumentException();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getCount() {
        return count;
    }

    public double getPrice() {
        return price;
    }
}
