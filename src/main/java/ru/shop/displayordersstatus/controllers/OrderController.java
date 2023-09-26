package ru.shop.displayordersstatus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shop.displayordersstatus.services.OrderServiceImpl;
import ru.shop.displayordersstatus.webClients.WebClientOkhttp;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4000"})
public class OrderController {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private WebClientOkhttp webClientOkhttp;

    @GetMapping
    @Async
    public CompletableFuture<ResponseEntity<String>> getOrdersWithConfiguration() throws IOException,
                                                                                                InterruptedException {
        // 1. Get data for ongoing modify
        String response = webClientOkhttp.getInformationAboutOrdersFromExternalResource();
        if (response.isEmpty()) {
            Thread.sleep(5000);
            getOrdersWithConfiguration();
        }
        // 2. Call method which call other inner methods which treat obj 'doc' and return json answer
        return CompletableFuture.completedFuture(orderService.getOrdersWithConfiguration(response));
    }
}