package org.surfsharing.postservice.Producer;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.surfsharing.postservice.dto.PostDto;

@Service
public class postProducer {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(postProducer.class);

    @Autowired
    private KafkaTemplate<String, PostDto> kafkaTemplate;

    public void sendMessage(PostDto event){
        LOGGER.info(String.format("Post event => %s", event.toString()));

        Message<PostDto> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "quickstart-events")
                .build();
        kafkaTemplate.send(message);
    }   
}