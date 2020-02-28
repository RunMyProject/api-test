package com.esabatini.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.esabatini.model.Product;
import com.esabatini.repository.ProductRepository;

@Service
public class ProductService {

    Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Inject
    private ProductRepository productRepository;
    
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByTitle(String title) {    	
        return productRepository.findByTitle(title);
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(String id) {
    	productRepository.deleteById(id);
    }

    public void deleteAll() {
    	productRepository.deleteAll();
    }
}