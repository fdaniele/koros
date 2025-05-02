package it.koros.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication {
    public static void main(String[] args) {
        System.out.println("ProducerApplication starting...");
        SpringApplication.run(ProducerApplication.class, args);
    }
}