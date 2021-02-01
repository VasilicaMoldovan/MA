package com.example.demo.controller;

import com.example.demo.model.Flower;
import com.example.demo.service.FlowerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/flowersApi")
public class FlowerController {
    private FlowerService flowerService;

    @GetMapping(value = "/getAll")
    public List<Flower> getAll() {
        return flowerService.getAll();
    }

    @GetMapping(value = "/getAllNames")
    public List<String> getAllFlowerNames() {
        System.out.println("cu florile");
        return flowerService.getFlowersNames();
    }

    @PostMapping(value = "/save", consumes = "application/json")
    public Long save(@RequestBody Flower flower) {
        return this.flowerService.save(flower);
    }

    @PutMapping(value = "/update", consumes = "application/json")
    public Flower update(@RequestBody Flower flower) {
        return this.flowerService.update(flower);
    }

    @DeleteMapping(value = "/delete/{id}")
    public boolean delete(@PathVariable(name = "id") Long id) {
        this.flowerService.deleteById(id);
        return true;
    }
}
