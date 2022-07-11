package com.example.rabbit.mulltiplerabbit.common;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
@Getter
@Slf4j
public class CommonResource {

    // config rabbit first
    @Value("${rabbit.first.spring.rabbitmq.host}")
    private String rabbitFirstHost;

    @Value("${rabbit.first.spring.rabbitmq.port}")
    private int rabbitFirstPort;

    @Value("${rabbit.first.spring.rabbitmq.username}")
    private String rabbitFirstUserName;

    @Value("${rabbit.first.spring.rabbitmq.password}")
    private String rabbitFirstPassword;

    // config rabbit second
    @Value("${rabbit.second.spring.rabbitmq.host}")
    private String rabbitSecondHost;

    @Value("${rabbit.second.spring.rabbitmq.port}")
    private int rabbitSecondPort;

    @Value("${rabbit.second.spring.rabbitmq.username}")
    private String rabbitSecondUserName;

    @Value("${rabbit.second.spring.rabbitmq.password}")
    private String rabbitSecondPassword;

    // information rabbit first
    @Value("${rabbit.first.queue.request}")
    private String rabbitFirstQueueRequest;

    @Value("${rabbit.first.queue.response}")
    private String rabbitFirstQueueResponse;

    @Value("${rabbit.first.exchange}")
    private String rabbitFirstExchange;

    // information rabbit second
    @Value("${rabbit.second.queue.request}")
    private String rabbitSecondQueueRequest;

    @Value("${rabbit.second.queue.response}")
    private String rabbitSecondQueueResponse;

    @Value("${rabbit.second.exchange}")
    private String rabbitSecondExchange;

    // setting timeout
    @Value("${timeout}")
    private int timeOutSeconds;

    // global variable
    private Map<String, Message> mapResponse;

    public void setMapResponse(String key, Message value) {
        this.mapResponse = new HashMap<>();
        this.mapResponse.put(key, value);
    }

    /**
     * Get message by contextId
     * if not found in specific seconds, return null
     * else remove in corresponding map and return message
     * @param corrId contextId of message
     * @return response proto
     */
    @SneakyThrows
    public Message waitAndReply(String corrId) {
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(timeOutSeconds); stop > System.nanoTime();) {
            if (Objects.isNull(mapResponse)) {
                continue;
            }
            if (mapResponse.containsKey(corrId)) {
                Message response = mapResponse.get(corrId);
                mapResponse.remove(corrId);
                return response;
            }
        }
        log.error("TimeoutException with corrId: {}", corrId);
        return new Message("".getBytes());
    }

    /**
     * Get message by contextId with timeout = 10 seconds
     * if not found in specific seconds, return null
     * else remove in corresponding map and return message
     * @param corrId contextId of message
     * @return response proto
     */
    @SneakyThrows
    public Message findMessage(String corrId) {
        return waitAndReply(corrId);
    }

}
