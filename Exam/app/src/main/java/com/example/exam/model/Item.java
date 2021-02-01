package com.example.exam.model;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("level")
    private int level;
    @SerializedName("status")
    private String status;
    @SerializedName("from")
    private int from;
    @SerializedName("to")
    private int to;
    private static int index;

    public Item(int id, String name, int level, String status, int from, int to) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.status = status;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", status='" + status + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
