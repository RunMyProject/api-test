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

import com.esabatini.model.User;
import com.esabatini.service.UserService;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    private UserService userService;

    @RequestMapping(value = "/findByFirstName/{firstName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public @ResponseBody User findByFirstName(@PathVariable(name = "firstName") String firstName) {
        return userService.findByFirstName(firstName);
    }

    @RequestMapping(value = "/findByLastName/{lastName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<User> findByLastName(@PathVariable(name = "lastName") String lastName) {
        return userService.findByLastName(lastName);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody User save(@RequestBody User user) {
		return userService.save(user);
    }
    
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public @ResponseBody Optional<User> findById(@PathVariable(name = "id") String id) {
        return userService.findById(id);
    }

    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteById(@PathVariable(name = "id") String id) {
		userService.deleteById(id);
    }

    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteAll() {
		userService.deleteAll();
    }
}