package com.example.tryexamorders.repository;

import com.example.tryexamorders.model.Order;

import java.util.List;

public interface Repository {
    List<Order> getAll();
    List<String> getAll2();
    boolean add(Order order);
    boolean delete(Order order);
    Order update(int id, String table, String details, String status, int time, String type);
}
