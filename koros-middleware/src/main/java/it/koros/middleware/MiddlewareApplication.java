package it.koros.middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class MiddlewareApplication {
    public static void main(String[] args) {
        System.out.println("MiddlewareApplication");
        SpringApplication.run(MiddlewareApplication.class, args);
    }
}
