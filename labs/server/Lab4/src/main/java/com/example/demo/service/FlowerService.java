package com.example.demo.service;
import com.example.demo.model.Flower;

import java.util.List;

public interface FlowerService {
    List<Flower> getAll();
    Long save(Flower flower);
    void deleteById(Long id);
    Flower update(Flower flower);
    List<String> getFlowersNames();
}
