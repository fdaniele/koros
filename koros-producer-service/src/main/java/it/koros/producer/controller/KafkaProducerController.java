package it.koros.producer.controller;

import it.koros.producer.utils.SecurityUtil;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/secure")
public class KafkaProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "service-exchange-messages-in";

    public KafkaProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send")
    public String sendSecureMessage(@RequestParam("payload") String payload) {
        //String messageSecured = SecurityUtil.generateSecureMessage(payload);
        String jwt = SecurityUtil.generateJwt(payload);
        String hmac = SecurityUtil.generateHmac(payload);
        long timestamp = System.currentTimeMillis();

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, payload);
        record.headers().add("x-sent-producer-timestamp", Long.toString(timestamp).getBytes(StandardCharsets.UTF_8));
        record.headers().add("x-jwt", jwt.getBytes(StandardCharsets.UTF_8));
        record.headers().add("x-hmac", hmac.getBytes(StandardCharsets.UTF_8));

        System.out.println("Invio messaggio sicuro a Kafka "+TOPIC);
        kafkaTemplate.send(record);
        return "Messaggio sicuro inviato a Kafka con headers di tracing e security";
    }
}
