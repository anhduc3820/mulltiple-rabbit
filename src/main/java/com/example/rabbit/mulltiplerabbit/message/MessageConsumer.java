package com.example.rabbit.mulltiplerabbit.message;

import com.example.rabbit.mulltiplerabbit.common.CommonResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    CommonResource commonResource;

    @RabbitListener(queues = "${rabbit.first.queue.response}", containerFactory = "containerFactoryRabbitFirst")
    public void consumerRabbitFirst(Message message) {
        log.info("{}", message);
        MessageProperties messageProperties = message.getMessageProperties();
        commonResource.setMapResponse(messageProperties.getCorrelationId(), message);
    }

    @RabbitListener(queues = "${rabbit.second.queue.response}", containerFactory = "containerFactoryRabbitSecond")
    public void consumerRabbitSecond(Message message) {
        log.info("{}", message);
        MessageProperties messageProperties = message.getMessageProperties();
        commonResource.setMapResponse(messageProperties.getCorrelationId(), message);
    }
}
