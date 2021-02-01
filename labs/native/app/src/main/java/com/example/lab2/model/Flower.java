package com.example.lab2.model;

import com.google.gson.annotations.SerializedName;

public class Flower {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("color")
    private String color;
    @SerializedName("number")
    private int number;
    @SerializedName("price")
    private double price;
    private static int index;

    public Flower(String name, String type, String color, int number, double price) {
        this.id = index;
        index++;
        this.name = name;
        this.type = type;
        this.color = color;
        this.number = number;
        this.price = price;
    }

    public Flower(int id, String name, String type, String color, int number, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
        this.number = number;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Flower{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", number=" + number +
                ", price=" + price +
                '}';
    }
}
