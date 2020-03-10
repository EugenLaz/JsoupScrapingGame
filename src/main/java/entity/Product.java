package entity;

import java.io.File;

public class Product {
    private final File photo;
    private final String description;
    private final int price;

    @Override
    public String toString() {
        return "Product{" +
                "description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    public Product(String description, int price, File photo) {
        this.photo = photo;
        this.description = description;
        this.price = price;
        System.out.println(photo.getAbsolutePath());
    }

    public File getPhoto() {
        return photo;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
