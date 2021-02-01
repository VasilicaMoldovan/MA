package com.example.demo.service;
import com.example.demo.model.Flower;
import com.example.demo.repository.FlowerRepository;
import com.example.demo.service.FlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowerServiceImpl implements FlowerService {
    private FlowerRepository flowerRepository;

    @Autowired
    public FlowerServiceImpl(FlowerRepository flowerRepository){
        this.flowerRepository = flowerRepository;
    }

    @Override
    public List<Flower> getAll() {
        return flowerRepository.findAll();
    }

    @Override
    @Transactional
    public Long save(Flower flower) {
        return flowerRepository.save(flower).getId();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        flowerRepository.deleteById(id);
    }

    @Override
    public Flower update(Flower flower) {
        return flowerRepository.save(flower);
    }

    @Override
    public List<String> getFlowersNames() {
        return flowerRepository.findAll().stream().map(Flower::getName).collect(Collectors.toList());
    }
}
