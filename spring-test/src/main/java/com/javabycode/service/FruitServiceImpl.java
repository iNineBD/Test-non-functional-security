package com.javabycode.service;

import com.javabycode.model.Fruit;
import com.javabycode.repository.FruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitServiceImpl implements FruitService {

    @Autowired
    private FruitRepository fruitRepository;

    @Override
    public List<Fruit> getAll(int offset, int count) {
        return fruitRepository.findAll(PageRequest.of(offset, count)).getContent();
    }

    @Override
    public Fruit findById(int id) {
        return fruitRepository.findById((long) id).orElse(null);
    }

    @Override
    public Fruit findByName(String name) {
        return fruitRepository.findByName(name);
    }

    @Override
    public void create(Fruit fruit) {
        fruitRepository.save(fruit);
    }

    @Override
    public void update(Fruit fruit) {
        fruitRepository.save(fruit);
    }

    @Override
    public void delete(int id) {
        fruitRepository.deleteById((long) id);
    }

    @Override
    public boolean exists(Fruit fruit) {
        return fruitRepository.existsById(fruit.getId());
    }
}
