package it.koros.service;

import it.koros.model.Message;
import it.koros.repository.MessageRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    private final MessageRepository messageRepository;

    public KafkaMessageListener(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "service-exchange-messages-out", groupId = "koros-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        String payload = record.value();
        long receivedTime = System.currentTimeMillis();

        Header producerTs = record.headers().lastHeader("x-sent-producer-timestamp");
        Header mwTs = record.headers().lastHeader("x-middleware-timestamp");

        if (producerTs != null) {
            long sentTime = Long.parseLong(new String(producerTs.value()));
            System.out.printf("Latenza totale Producer > Consumer: %d ms%n", (receivedTime - sentTime));
        }

        if (mwTs != null) {
            long mwTime = Long.parseLong(new String(mwTs.value()));
            System.out.printf("Latenza Middleware > Consumer: %d ms%n", (receivedTime - mwTime));
        }

        System.out.printf("Messaggio ricevuto: '%s'%n", payload);

        //Salvataggio messaggio su mongoDB
        messageRepository.save(new Message(payload));
    }
}