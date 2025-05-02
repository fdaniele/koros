package it.koros.middleware.service;

import it.koros.middleware.utils.SecurityUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class MiddlewareListenerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${middleware.topic.out}")
    private String topicOut;

    @Value("${middleware.topic.invalid}")
    private String topicInvalid;

    public MiddlewareListenerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${middleware.topic.in}", groupId = "koros-middleware-group")
    public void handleMessage(ConsumerRecord<String, String> record) {
        String payload = record.value();
        Headers headers = record.headers();

        Header jwtHeader = headers.lastHeader("x-jwt");
        Header hmacHeader = headers.lastHeader("x-hmac");

        //Controllo presenza headers
        if (jwtHeader == null || hmacHeader == null) {
            System.out.println("Header di security mancanti. Messaggio scartato e inviato al topic "+topicInvalid);
            kafkaTemplate.send(new ProducerRecord<>(topicInvalid, payload));
            return;
        }

        String jwt = new String(jwtHeader.value(), StandardCharsets.UTF_8);
        String hmac = new String(hmacHeader.value(), StandardCharsets.UTF_8);

        System.out.println("## Recuperati i headers di security ##");
        System.out.println("JWT: "+jwt);
        System.out.println("HMAC: "+hmac);

        //controllo validita headers
        if (!SecurityUtil.verifyHmac(payload, hmac) || !SecurityUtil.verifyJwt(jwt)) {
            System.out.println("Verifica security fallita. Routing su topic INVALID "+topicInvalid);
            kafkaTemplate.send(new ProducerRecord<>(topicInvalid, payload));
            return;
        }

        ProducerRecord<String, String> outRecord = new ProducerRecord<>(topicOut, payload);
        outRecord.headers().add("x-jwt", jwt.getBytes(StandardCharsets.UTF_8));
        outRecord.headers().add("x-hmac", hmac.getBytes(StandardCharsets.UTF_8));

        Header sentTs = headers.lastHeader("x-sent-producer-timestamp");
        if (sentTs != null) outRecord.headers().add("x-sent-producer-timestamp", sentTs.value());
        outRecord.headers().add("x-middleware-timestamp", Long.toString(System.currentTimeMillis()).getBytes());

        //invio al consumer service
        System.out.println("## Passati tutti i controlli di security e invio al topic "+topicOut+" ##");
        kafkaTemplate.send(outRecord);
    }
}
