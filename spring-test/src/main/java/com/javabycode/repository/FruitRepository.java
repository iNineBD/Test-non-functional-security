package com.javabycode.repository;

import com.javabycode.model.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FruitRepository extends JpaRepository<Fruit, Long> {
    Fruit findByName(String name);
}
