package it.koros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        System.out.println("ConsumerApplication starting...");
        SpringApplication.run(ConsumerApplication.class, args);
    }
}