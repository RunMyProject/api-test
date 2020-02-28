package com.esabatini.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.esabatini.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

  public List<Product> findByTitle(String title);
  
}