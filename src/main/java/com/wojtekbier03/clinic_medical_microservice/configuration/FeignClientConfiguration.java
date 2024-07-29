package com.wojtekbier03.clinic_medical_microservice.configuration;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, SECONDS.toMillis(1), 5);
    }
}

