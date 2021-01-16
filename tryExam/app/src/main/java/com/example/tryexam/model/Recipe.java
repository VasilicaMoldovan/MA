package com.example.tryexam.model;


import com.google.gson.annotations.SerializedName;

public class Recipe {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("details")
    private String details;
    @SerializedName("time")
    private int time;
    @SerializedName("type")
    private String type;
    @SerializedName("rating")
    private int rating;
    private static int index;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public int getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public int getRating() {
        return rating;
    }

    public Recipe(int id, String name, String details, int time, String type, int rating) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.time = time;
        this.type = type;
        this.rating = rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", time=" + time +
                ", type='" + type + '\'' +
                ", rating=" + rating +
                '}';
    }
}
