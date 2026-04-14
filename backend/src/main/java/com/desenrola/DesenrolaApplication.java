package com.desenrola;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesenrolaApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DesenrolaApplication.class, args);
        System.out.println("🚀 Desenrola Backend iniciado em http://localhost:8080");
    }
}
