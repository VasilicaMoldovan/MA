package com.example.lab2.repository;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.lab2.model.Flower;

import java.util.ArrayList;
import java.util.List;

public class FlowerLabRepository implements Repository {
    private List<Flower> flowerList;

    public FlowerLabRepository(List<Flower> flowerList) {
        this.flowerList = flowerList;
    }

    public FlowerLabRepository() {
        flowerList = new ArrayList<>();
        flowerList.add(new Flower("Tulip", "Thread", "White", 23, 102));
    }



    @Override
    public List<Flower> getAll() {
        return flowerList;
    }

    @Override
    public List<String> getAllFlowerNames() {
        List<String> flowerNames = new ArrayList<>();
        for (Flower flower : flowerList) {
            flowerNames.add(flower.getName());
        }
        return flowerNames;
    }

    @Override
    public boolean addFlower(Flower newFlower) {
        //Flower newFlower = new Flower(name, type, color, number, price);
        return flowerList.add(newFlower);
    }

    @Override
    public boolean deleteFlower(Flower flower) {
        return flowerList.remove(flower);
    }

    @Override
    public Flower updateFlower(int id, String name, String type, String color, int number, double price) {
        for(int i = 0; i < flowerList.size(); i++) {
            if (flowerList.get(i).getId() == id) {
                flowerList.get(i).setName(name);
                flowerList.get(i).setType(type);
                flowerList.get(i).setColor(color);
                flowerList.get(i).setNumber(number);
                flowerList.get(i).setPrice(price);

                return flowerList.get(i);
            }
        }
        return null;
    }
}
