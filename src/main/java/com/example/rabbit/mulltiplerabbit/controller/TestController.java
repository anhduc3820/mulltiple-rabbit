package com.example.rabbit.mulltiplerabbit.controller;

import com.example.rabbit.mulltiplerabbit.service.TestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping(value = "/demo")
    public void sendRequest() {
        testService.sendRabbitSecondAndReceivedMessage();
    }

    @GetMapping(value = "/send_quote")
    public void sendQuote() {
        testService.sendQuote();
    }
}
