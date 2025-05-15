package com.thcme.matchengine.controller;

import com.thcme.matchengine.datamodel.Order;
import com.thcme.matchengine.service.impl.OrderSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderSubmissionController {
    @Autowired
    private OrderSubmissionService orderSubmissionService;
    
    // Endpoint to add  an order
    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        try{
            Order aggregatedOrder = orderSubmissionService.addOrder(order);
            return new ResponseEntity<>(aggregatedOrder, HttpStatus.CREATED);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to add  an order
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        try{
            return new ResponseEntity<>("Pong", HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    
}
