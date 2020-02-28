package com.esabatini.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.esabatini.model.Order;
import com.esabatini.model.OrderParams;
import com.esabatini.model.OrderProductParams;
import com.esabatini.model.OrderResponse;
import com.esabatini.service.OrderService;

@RestController
@RequestMapping(value = "api/order")
public class OrderController {

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Inject
    private OrderService orderService;
    
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Async
    public CompletableFuture<List<Order>> findAll() {
        return CompletableFuture.completedFuture(orderService.findAll());
    }
    
    @RequestMapping(value = "/findByTitle/{title}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Async
    public CompletableFuture<List<Order>> findByTitle(@PathVariable(name = "title") String title) {
        return CompletableFuture.completedFuture(orderService.findByTitle(title));
    }
    
    // createOrder (name, date, product, user)
    // add the product and set the status to "open"
    // sync REST
    //
    @RequestMapping(value = "/createOrderSync", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody OrderResponse createOrderSync(@RequestBody OrderParams orderParams) {
        return orderService.saveByOrderParams(orderParams);
    }
    
    // createOrder (name, date, product, user)
    // add the product and set the status to "open"
    // async REST
    //
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Async
    public @ResponseBody CompletableFuture<OrderResponse> createOrder(@RequestBody OrderParams orderParams) {
        return CompletableFuture.completedFuture(orderService.saveByOrderParams(orderParams));
    }
    
    @RequestMapping(value = "/findById_user/{id_user}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Async
    public CompletableFuture<List<Order>> findById_user(@PathVariable(name = "id_user") String id_user) {
        return CompletableFuture.completedFuture(orderService.findById_user(id_user));
    }

    @RequestMapping(value = "/closeOrder", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Async
    public @ResponseBody CompletableFuture<Order> closeOrder(@RequestBody Order order) {
        return CompletableFuture.completedFuture(orderService.close(order));
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Async
    public @ResponseBody CompletableFuture<OrderResponse> addProduct(@RequestBody OrderProductParams orderProductParams) {
        return CompletableFuture.completedFuture(orderService.addProduct(orderProductParams));
    }

    @RequestMapping(value = "/delProduct", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Async
    public @ResponseBody CompletableFuture<OrderResponse> delProduct(@RequestBody OrderProductParams orderProductParams) {
        return CompletableFuture.completedFuture(orderService.delProduct(orderProductParams));
    }

    @RequestMapping(value = "/nextStepOrder/{id_order}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Async
    public @ResponseBody CompletableFuture<Order> nextStepOrder(@PathVariable(name = "id_order") String id_order) {
        return CompletableFuture.completedFuture(orderService.nextStepOrder(id_order));
    }

    @RequestMapping(value = "/refundOrder/{id_order}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Async
    public @ResponseBody CompletableFuture<Order> refundOrder(@PathVariable(name = "id_order") String id_order) {
        return CompletableFuture.completedFuture(orderService.refundOrder(id_order));
    }

}