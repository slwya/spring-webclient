package com.example.springwebclient.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public class WebClientUtil {
    /** Log */
    Logger log = LoggerFactory.getLogger(getClass());

    /* webclient */
    private final WebClient webClient;

    public WebClientUtil(WebClient webClient) {
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
     * GET - Blocking 호출
     *
     * @param endPoint     API EndPoint 정보
     * @param params       API 파라미터
     * @param mediaType    데이터 받을 미디어 타입
     * @param elementClass 데이터 받을 변수타입
     * @return 호출결과
     */
    public <T> T getSync(EndPoint endPoint,
                         MultiValueMap<String, String> params,
                         MediaType mediaType,
                         Class<T> elementClass) {
        return getWebClient(endPoint)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(endPoint.uri())
                        .queryParams(params)
                        .build())
                .accept(mediaType)
                .retrieve()
                .bodyToMono(elementClass)
                .doOnError(throwable -> log.error("[getSync] - errorMsg: {}", throwable.getMessage()))
                .block();
    }

    /**
     * GET - Non-Blocking 호출
     *
     * @param endPoint       API EndPoint 정보
     * @param params         API 파라미터
     * @param mediaType      데이터 받을 미디어 타입
     * @param elementClass   데이터 저장 변수타입
     * @param resultMapKey   API 호출결과 저장키
     * @param resultMap      API 호출결과 저장 Map(ConcurrentMap)
     * @param countDownLatch API별 호출 스레드 카운트 다운 객체
     */
    public <T> void getAsync(EndPoint endPoint,
                             MultiValueMap<String, String> params,
                             MediaType mediaType,
                             Class<T> elementClass,
                             String resultMapKey,
                             ConcurrentMap<String, Object> resultMap,
                             CountDownLatch countDownLatch) {
        getWebClient(endPoint)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(endPoint.uri())
                        .queryParams(params)
                        .build())
                .accept(mediaType)
                .retrieve()
                .bodyToMono(elementClass)
                .doOnError(throwable -> {
                    log.error("[getAsync] - errorMsg: {}", throwable.getMessage());
                    countDownLatch.countDown();
                })
                .subscribe(apiCallResult -> {
                    resultMap.put(resultMapKey, apiCallResult);
                    countDownLatch.countDown();
                });
    }

    /**
     * POST - Blocking 호출
     *
     * @param endPoint     API EndPoint 정보
     * @param params       API 파라미터
     * @param mediaType    데이터 받을 미디어 타입
     * @param elementClass 데이터 받을 변수타입
     * @return 호출결과
     */
    public <T> T postSync(EndPoint endPoint,
                          MultiValueMap<String, String> params,
                          MediaType mediaType,
                          Class<T> elementClass) {
        return getWebClient(endPoint)
                .post()
                .uri(endPoint.uri())
                .accept(mediaType)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(elementClass)
                .doOnError(throwable -> log.error("[postSync] - errorMsg: {}", throwable.getMessage()))
                .block();
    }

    /**
     * POST - Non-Blocking 호출
     *
     * @param endPoint       API EndPoint 정보
     * @param params         API 파라미터
     * @param mediaType      데이터 받을 미디어 타입
     * @param elementClass   데이터 저장 변수타입
     * @param resultMapKey   API 호출결과 저장키
     * @param resultMap      API 호출결과 저장 Map(ConcurrentMap)
     * @param countDownLatch API별 호출 스레드 카운트 다운 객체
     */
    public <T> void postAsync(EndPoint endPoint,
                              MultiValueMap<String, String> params,
                              MediaType mediaType,
                              Class<T> elementClass,
                              String resultMapKey,
                              ConcurrentMap<String, Object> resultMap,
                              CountDownLatch countDownLatch) {
        getWebClient(endPoint)
                .post()
                .uri(endPoint.uri())
                .accept(mediaType)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(elementClass)
                .doOnError(throwable -> {
                    log.error("[postAsync] - errorMsg: {}", throwable.getMessage());
                    countDownLatch.countDown();
                })
                .subscribe(apiCallResult -> {
                    resultMap.put(resultMapKey, apiCallResult);
                    countDownLatch.countDown();
                });
    }
}
