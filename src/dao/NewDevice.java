package dao;

public class NewDevice {
    private byte[] image;
    private String manufacturer;
    private String model;
    private String description;
    private double price;

    public NewDevice(byte[] image, String manufacturer, String model, String description, double price) {
        this.image = image;
        this.manufacturer = manufacturer;
        this.model = model;
        this.description = description;
        this.price = price;

        if (image == null || manufacturer.equals("") || model.equals("")) throw new IllegalArgumentException();
    }

    public byte[] getImage() {
        return image;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
