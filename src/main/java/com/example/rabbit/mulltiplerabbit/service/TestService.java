package com.example.rabbit.mulltiplerabbit.service;

import com.example.rabbit.mulltiplerabbit.common.CommonResource;
import com.example.rabbit.mulltiplerabbit.message.MessagePublisher;
import com.example.rabbit.mulltiplerabbit.utils.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TestService {

    @Autowired
    CommonResource commonResource;

    @Autowired
    Snowflake snowflake;

    @Autowired
    MessagePublisher messagePublisher;

    public void sendRabbitSecondAndReceivedMessage() {
        String corrId = String.valueOf(snowflake.nextId());

        String messageSend = "aaaaaaaaaaaaa";

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReplyTo(commonResource.getRabbitFirstQueueResponse());
        messageProperties.setCorrelationId(corrId);
        messageProperties.setContentType(messageSend);

        messagePublisher.publishQueueRabbitSecond(commonResource.getRabbitSecondQueueResponse(), messageSend.getBytes(), messageProperties);
        log.info("Send message to rabbit second success with corrId: {}", corrId);

        replyRabbitFirst(messageProperties);

        Message messageReply = commonResource.findMessage(corrId);
        log.info("Received message reply from rabbit second with message: {}, and body: {}", messageReply.getMessageProperties().getContentType(), messageReply.getBody());
    }

    public void replyRabbitFirst(MessageProperties messageProperties) {
        String messageSend = "bbbbbbbbbbbbbbb";
        messagePublisher.publishQueueRabbitFirst(messageProperties.getReplyTo(), messageSend.getBytes(), messageProperties);
        log.info("Send message to rabbit first success with corrId: {}", messageProperties.getCorrelationId());
    }
}
