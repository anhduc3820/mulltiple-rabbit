package com.example.rabbit.mulltiplerabbit.service;

import ch.qos.logback.core.util.TimeUtil;
import com.example.rabbit.mulltiplerabbit.common.CommonResource;
import com.example.rabbit.mulltiplerabbit.message.MessagePublisher;
import com.example.rabbit.mulltiplerabbit.protobuff.PricingModel;
import com.example.rabbit.mulltiplerabbit.protobuff.PricingService;
import com.example.rabbit.mulltiplerabbit.protobuff.Rpc;
import com.example.rabbit.mulltiplerabbit.utils.Snowflake;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;


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

    @SneakyThrows
    public void sendQuote() {
        while (true) {
            Random generator = new Random(19900828);
            double value = generator.nextDouble() * 360.0;

            PricingModel.Quote quote = PricingModel.Quote.newBuilder()
                    .setSymbolCode("1305")
                    .setCurrentPrice(String.valueOf(value))
                    .setExchangeCode("TSE")
                    .build();

            PricingService.QuoteEvent quoteEvent = PricingService.QuoteEvent.newBuilder()
                    .addQuote(quote)
                    .build();

            Rpc.RpcMessage rpcMessage = Rpc.RpcMessage.newBuilder()
                    .setPayloadClass(Rpc.RpcMessage.Payload.QUOTE_EVENT)
                    .setPayloadData(quoteEvent.toByteString())
                    .build();

            messagePublisher.publishMessageRabbitSecond("PSP_QUOTE", rpcMessage.toByteArray());
            System.out.println("Success send message with value:" + value);
            TimeUnit.MILLISECONDS.sleep(10);
        }
    }
}
