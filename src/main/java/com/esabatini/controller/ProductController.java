package com.esabatini.controller;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.esabatini.model.Product;
import com.esabatini.service.ProductService;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Inject
    private ProductService productService;

    @RequestMapping(value = "/findByTitle/{title}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public @ResponseBody List<Product> findByTitle(@PathVariable(name = "title") String title) {
        return productService.findByTitle(title);
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Product save(@RequestBody Product product) {
		return productService.save(product);
    }
    
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public @ResponseBody Optional<Product> findById(@PathVariable(name = "id") String id) {
        return productService.findById(id);
    }

    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteById(@PathVariable(name = "id") String id) {
        productService.deleteById(id);
    }
    
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Product> findAll() {
        return productService.findAll();
    }
    
    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteAll() {
    	productService.deleteAll();
    }
    
}