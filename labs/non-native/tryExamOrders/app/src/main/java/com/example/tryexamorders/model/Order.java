package com.example.tryexamorders.model;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    private int id;
    @SerializedName("table")
    private String table;
    @SerializedName("details")
    private String details;
    @SerializedName("status")
    private String status;
    @SerializedName("time")
    private int time;
    @SerializedName("type")
    private String type;
    private static int index;

    public Order(int id, String table, String details, String status, int time, String type) {
        this.id = id;
        this.table = table;
        this.details = details;
        this.status = status;
        this.time = time;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", table='" + table + '\'' +
                ", details='" + details + '\'' +
                ", status='" + status + '\'' +
                ", time=" + time +
                ", type='" + type + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
