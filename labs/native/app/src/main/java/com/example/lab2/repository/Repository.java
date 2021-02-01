package com.example.lab2.repository;

import com.example.lab2.model.Flower;

import java.util.List;

public interface Repository {
    List<Flower> getAll();
    List<String> getAllFlowerNames();
    boolean addFlower(Flower flower);
    boolean deleteFlower(Flower flower);
    Flower updateFlower(int id, String name, String type, String color, int number, double price);
}
