package com.challenge.vasconcelos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import feign.Logger;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class VasconcelosCryptoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VasconcelosCryptoApplication.class, args);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
