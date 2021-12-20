package com.example.thb3;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String dateImport;
    private int quantityWH;
    private String producer;
    private String price;

    public Product(String id, String name, String dateImport, int quantityWH, String producer, String price) {
        this.id = id;
        this.name = name;
        this.dateImport = dateImport;
        this.quantityWH = quantityWH;
        this.producer = producer;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateImport() {
        return dateImport;
    }

    public int getQuantityWH() {
        return quantityWH;
    }

    public String getProducer() {
        return producer;
    }

    public String getPrice() {
        return price;
    }
}
