package com.example.springwebclient.global;

public enum ApiEndPoint implements EndPoint{
    // 다나와자동차 컨텐츠 조회 API
    GET_API_PATH("/graphiql");

    private final String uri;

    private final Long timeout;

    ApiEndPoint(String uri) {
        this.uri = uri;
        this.timeout = null;
    }

    ApiEndPoint(String uri, Long timeout) {
        this.uri = uri;
        this.timeout = timeout;
    }

    @Override
    public String uri() {
        return uri;
    }

    @Override
    public Long timeout() {
        return timeout;
    }
}
