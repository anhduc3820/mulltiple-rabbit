package com.example.rabbit.mulltiplerabbit.message;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessagePublisher {

    @Resource(name = "rabbitTemplateFirst")
    private RabbitTemplate rabbitTemplateRabbitFirst;

    @Resource(name = "rabbitTemplateSecond")
    private RabbitTemplate rabbitTemplateRabbitSecond;

    // rabbit first
    /**
     * Send message through exchange
     * @param exchangeName: exchangeName to send message
     * @param data: data in byte[]
     */
    public void publishMessageRabbitFirst(String exchangeName, final byte[] data) {
        rabbitTemplateRabbitFirst.convertAndSend(exchangeName, "", new Message(data));
    }

    /**
     * Send message through queue with messageProperties
     * This function will send through default exchange (name="")
     * @param queueName: queue to send message
     * @param data: data in byte[]
     * @param properties: Message Properties for setting message-queue settings
     */
    public void publishQueueRabbitFirst(String queueName, final byte[] data, MessageProperties properties) {
        rabbitTemplateRabbitFirst.convertAndSend("", queueName, new Message(data, properties));
    }

    /**
     * Send message through queue without messageProperties
     * This function will send through default exchange (name="")
     * @param queueName: queue to send message
     * @param data: data in byte[]
     */
    public void publishQueueRabbitFirst(String queueName, final byte[] data) {
        rabbitTemplateRabbitFirst.convertAndSend("", queueName, new Message(data));
    }

    // rabbit second
    /**
     * Send message through exchange
     * @param exchangeName: exchangeName to send message
     * @param data: data in byte[]
     */
    public void publishMessageRabbitSecond(String exchangeName, final byte[] data) {
        rabbitTemplateRabbitSecond.convertAndSend(exchangeName, "", new Message(data));
    }

    /**
     * Send message through queue with messageProperties
     * This function will send through default exchange (name="")
     * @param queueName: queue to send message
     * @param data: data in byte[]
     * @param properties: Message Properties for setting message-queue settings
     */
    public void publishQueueRabbitSecond(String queueName, final byte[] data, MessageProperties properties) {
        rabbitTemplateRabbitSecond.convertAndSend("", queueName, new Message(data, properties));
    }

    /**
     * Send message through queue without messageProperties
     * This function will send through default exchange (name="")
     * @param queueName: queue to send message
     * @param data: data in byte[]
     */
    public void publishQueueRabbitSecond(String queueName, final byte[] data) {
        rabbitTemplateRabbitSecond.convertAndSend("", queueName, new Message(data));
    }
}
