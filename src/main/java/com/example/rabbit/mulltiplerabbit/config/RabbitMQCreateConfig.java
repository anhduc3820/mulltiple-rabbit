package com.example.rabbit.mulltiplerabbit.config;

import com.example.rabbit.mulltiplerabbit.common.CommonResource;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Create Queue, Exchange, and Bind Relationships
 * Created by AnhDuc on 2022/7/11.
 */
@Configuration
public class RabbitMQCreateConfig {

    @Autowired
    CommonResource commonResource;

    @Resource(name = "rabbitAdminFirst")
    private RabbitAdmin rabbitAdminFirst;

    @Resource(name = "rabbitAdminSecond")
    private RabbitAdmin rabbitAdminSecond;

    @PostConstruct
    public void RabbitInit() {
        //-------------------------------------------------------------------------------------------------//
        //-------------------------------------------------------------------------------------------------//
        //--------------------------------------Init Rabbit First------------------------------------------//
        //-------------------------------------------------------------------------------------------------//
        //-------------------------------------------------------------------------------------------------//

        rabbitAdminFirst.declareQueue(new Queue(commonResource.getRabbitFirstQueueRequest(), true));
        rabbitAdminFirst.declareQueue(new Queue(commonResource.getRabbitFirstQueueResponse(), true));
        rabbitAdminFirst.declareExchange(new TopicExchange(commonResource.getRabbitFirstExchange(), true, false));
        rabbitAdminFirst.declareBinding(
                BindingBuilder
                        .bind(new Queue(commonResource.getRabbitFirstQueueRequest(), true))  //Create Queue Directly
                        .to(new TopicExchange(commonResource.getRabbitFirstExchange(), true, false)) //Create switches directly to establish associations
                        .with("testKey"));    //Specify Routing Key

        //-------------------------------------------------------------------------------------------------//
        //-------------------------------------------------------------------------------------------------//
        //--------------------------------------Init Rabbit Second-----------------------------------------//
        //-------------------------------------------------------------------------------------------------//
        //-------------------------------------------------------------------------------------------------//

        rabbitAdminSecond.declareQueue(new Queue(commonResource.getRabbitSecondQueueRequest(), true));
        rabbitAdminSecond.declareQueue(new Queue(commonResource.getRabbitSecondQueueResponse(), true));
        rabbitAdminSecond.declareExchange(new TopicExchange(commonResource.getRabbitSecondExchange(), true, false));
        rabbitAdminSecond.declareBinding(
                BindingBuilder
                        .bind(new Queue(commonResource.getRabbitSecondQueueRequest(), true))  //Create Queue Directly
                        .to(new TopicExchange(commonResource.getRabbitSecondExchange(), true, false)) //Create switches directly to establish associations
                        .with("testKey"));    //Specify Routing Key
    }
}
