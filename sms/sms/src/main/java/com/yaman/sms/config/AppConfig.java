package com.yaman.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Uncomment this block if you want to MOCK for testing
    /*
    @Bean
    public RestTemplate mockRestTemplate() {
        return new RestTemplate() {
            @Override
            public <T> ResponseEntity<T> exchange(String url, org.springframework.http.HttpMethod method,
                                                  org.springframework.http.HttpEntity<?> requestEntity,
                                                  Class<T> responseType, Object... uriVariables) {
                // Always return SUCCESS response
                return new ResponseEntity<>((T) "{\"message\":\"SMS sent (mocked)\"}", HttpStatus.OK);
            }
        };
    }
    */
}
