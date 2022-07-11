package com.example.rabbit.mulltiplerabbit.config;

import com.example.rabbit.mulltiplerabbit.common.CommonResource;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by AnhDuc on 2022/07/11.
 */
@Configuration
public class MultipleRabbitMQConfig {
    
    @Autowired
    CommonResource commonResource;

    // mq primary connection
    @Bean(name = "connectionFactoryRabbitFirst")
    @Primary
    public CachingConnectionFactory connectionFactoryRabbitFirst() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(commonResource.getRabbitFirstHost());
        connectionFactory.setPort(commonResource.getRabbitFirstPort());
        connectionFactory.setUsername(commonResource.getRabbitFirstUserName());
        connectionFactory.setPassword(commonResource.getRabbitFirstPassword());
        return connectionFactory;
    }

    @Bean(name = "rabbitTemplateFirst")
    @Primary
    public RabbitTemplate rabbitTemplateFirst(@Qualifier("connectionFactoryRabbitFirst") ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean(name = "containerFactoryRabbitFirst")
    @Primary
    public SimpleRabbitListenerContainerFactory listenerContainerRabbitFirst(@Qualifier("connectionFactoryRabbitFirst") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean(name = "rabbitAdminFirst")
    @Primary
    public RabbitAdmin initRabbitAdminFirst(
            @Qualifier("connectionFactoryRabbitFirst") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
    
    // connection rabbit second
    @Bean(name = "connectionFactoryRabbitSecond")
    public CachingConnectionFactory connectionFactoryRabbitSecond() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(commonResource.getRabbitSecondHost());
        connectionFactory.setPort(commonResource.getRabbitSecondPort());
        connectionFactory.setUsername(commonResource.getRabbitSecondUserName());
        connectionFactory.setPassword(commonResource.getRabbitSecondPassword());
        return connectionFactory;
    }

    @Bean(name = "rabbitTemplateSecond")
    public RabbitTemplate rabbitTemplateSecond(@Qualifier("connectionFactoryRabbitSecond") ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean(name = "containerFactoryRabbitSecond")
    public SimpleRabbitListenerContainerFactory listenerContainerRabbitSecond(@Qualifier("connectionFactoryRabbitSecond") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean(name = "rabbitAdminSecond")
    public RabbitAdmin initRabbitAdminSecond(
            @Qualifier("connectionFactoryRabbitSecond") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
}
