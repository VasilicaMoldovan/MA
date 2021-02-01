package com.example.exam.repository;

import com.example.exam.model.Item;

import java.util.List;

public interface Repository {
    List<Item> getAll();
    boolean add(Item item);
    boolean delete(Item item);
    Item update(int id, String name, int level, String status, int from, int to);
}

