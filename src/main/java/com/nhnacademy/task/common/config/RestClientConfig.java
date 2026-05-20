//package com.nhnacademy.task.common.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestClient;
//
//@Configuration
//public class RestClientConfig {
//    @Value("${services.account-api.url}")
//    private String accountApiUrl;
//
//    @Bean
//    public RestClient restClient() {
//        return RestClient.builder()
//                .baseUrl(accountApiUrl)
//                .build();
//    }
//}