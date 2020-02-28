package com.esabatini.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.esabatini.model.Order;
import com.esabatini.model.Status;

public interface OrderRepository extends MongoRepository<Order, String> {

  public List<Order> findByTitle(String title);

  @Query(value="{ 'id_user' : ?0 }")
  public List<Order> findById_user(String id_user);

  @Query(value="{ 'status' : ?0 }")
  public List<Order> findByStatus(Status status);

}