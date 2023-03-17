package com.example.springwebclient.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * <pre>
 * webClientConfig
 * spring 5.x부터 지원 restTemplate와 다르게 비동기처리도 지원한다.
 * 효율적인 메모리사용하기위해 configuration + bean으로 설정
 * </pre>
 */

@Configuration
public class WebClientConfig {

    @Value("${api.url}")
    private String apiUrl;

    @Bean
    public WebClient webClient() {
        return webClientFactory(apiUrl);
    }

    public WebClient webClientFactory(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(5000));  // timeout

        return WebClient
                .builder()
                .baseUrl(baseUrl)
                // Request Data를 버퍼링하기 위한 메모리의 기본값은 256KB -> 2MB로 변경
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
