package com.esabatini.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.esabatini.model.User;
import com.esabatini.repository.UserRepository;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

	@Inject
	public UserRepository userRepository;
	
	public List<User> findAll() {
        return userRepository.findAll();
    }
	
    public User findByFirstName(String firstName) {
        return userRepository.findByFirstName(firstName);
    }

    public List<User> findByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    public User save(User user) {
    	return userRepository.save(user);
    }

    public User insert(User user) {
    	return userRepository.insert(user);
    }

    public Optional<User> findById(String id) {    	
    	return userRepository.findById(id);
    }
    
    public void deleteById(String id) {
    	userRepository.deleteById(id);
    }
    
    public void deleteAll() {
    	userRepository.deleteAll();
    }
}
