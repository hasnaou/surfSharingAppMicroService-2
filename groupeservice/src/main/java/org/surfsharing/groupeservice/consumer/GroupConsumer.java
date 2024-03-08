package org.surfsharing.groupeservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.surfsharing.groupeservice.dto.GroupeDto;
import org.surfsharing.groupeservice.entity.Groupe;
import org.surfsharing.groupeservice.service.IGroupeService;
import org.surfsharing.postservice.dto.PostDto;

@Service
public class GroupConsumer {

    @Autowired
    private IGroupeService groupeService;

    @KafkaListener(topics = "quickstart-events", groupId = "myGroup", containerFactory = "kafkaListenerContainerFactory")
    public void consume(PostDto postDto) {
       GroupeDto group = groupeService.getGroupeById(postDto.getGroupeId());
       groupeService.deleteGroupe(postDto.getGroupeId(),group.getAdminId());
       System.out.println("Consumed message: upp" + postDto);
    }
}