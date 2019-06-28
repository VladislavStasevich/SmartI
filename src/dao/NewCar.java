package dao;

public class NewCar {
    private byte[] image;
    private String manufacturer;
    private String model;
    private String engine;
    private String transmission;
    private int year;
    private String description;
    private double price;

    public NewCar(byte[] image, String manufacturer, String model, String engine, String transmission, int year, String description, double price) {
        this.image = image;
        this.manufacturer = manufacturer;
        this.model = model;
        this.engine = engine;
        this.transmission = transmission;
        this.year = year;
        this.description = description;
        this.price = price;

        if (image == null || manufacturer.equals("") || model.equals("")) throw new IllegalArgumentException();
    }

    public byte[] getImage() {
        return image;
    }

    public String getEngine() {
        return engine;
    }

    public String getTransmission() {
        return transmission;
    }

    public int getYear() {
        return year;
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
