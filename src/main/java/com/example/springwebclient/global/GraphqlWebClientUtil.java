package com.example.springwebclient.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Map;

public class GraphqlWebClientUtil {
    /** Log */
    Logger log = LoggerFactory.getLogger(getClass());

    /* webclient */
    private final WebClient webClient;

    public GraphqlWebClientUtil(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * webClient 가져오기(api별 타입아웃 체크)
     *
     * @param endPoint url endPoint
     * @return webClient
     */
    private WebClient getWebClient(EndPoint endPoint) {
        if (endPoint.timeout() == null) {
            return this.webClient;
        } else {
            // api 별 timeout 존재할경우 timeout 설정
            return this.webClient
                    .mutate()
                    .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                            .responseTimeout(Duration.ofMillis(endPoint.timeout()))))
                    .build();
        }
    }


    /**
     * POST - Blocking 호출
     *
     * @param endPoint     API EndPoint 정보
     * @param document     query
     * @param name         name
     * @param elementClass 데이터 받을 변수타입
     * @return 호출결과
     */
    public <T> T postSync(EndPoint endPoint,
                          String document,
                          String name,
                          Map<String, Object> varMap,
                          Class<T> elementClass) {

        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(getWebClient(endPoint)).build();
        return graphQlClient
                .document(document)
                .variables(varMap)
                .retrieve(name)
                .toEntity(elementClass)
                .block();
    }

    /**
     * POST - Blocking 호출
     *
     * @param endPoint     API EndPoint 정보
     * @param document     query
     * @param name         name
     * @param elementClass 데이터 받을 변수타입
     * @return 호출결과
     */
    public <T> java.util.List<T> postListSync(EndPoint endPoint,
                                              String document,
                                              String name,
                                              Map<String, Object> varMap,
                                              Class<T> elementClass) {

        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(getWebClient(endPoint)).build();
        return graphQlClient
                .document(document)
                .variables(varMap)
                .retrieve(name)
                .toEntityList(elementClass)
                .block();
    }

}
