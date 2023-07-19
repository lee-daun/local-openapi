package com.local.openapi.core.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.local.openapi.configurations.properties.KakaoClientConfigurationProperties;
import com.local.openapi.configurations.properties.RestClientConfigurationProperties;
import com.local.openapi.core.exception.KakaoException;
import com.local.openapi.core.exception.ServiceException;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;


import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.http.HttpStatus.*;

@Slf4j
public class KakaoClientImpl extends ClientSupport implements KakaoClient{
    private KakaoClientConfigurationProperties config;
    private ObjectMapper objectMapper;
    public KakaoClientImpl(KakaoClientConfigurationProperties config) {
        super(config);
        this.config = config;
        objectMapper=new ObjectMapper();
    }
    @Override
    protected Consumer<HttpHeaders> httpHeaders(RestClientConfigurationProperties config) {
        KakaoClientConfigurationProperties configurationProperties = ((KakaoClientConfigurationProperties) config);

        return headers -> headers.set("Authorization", "KakaoAK "+configurationProperties.getApikey());
    }


    @Override
    public SearchResponse localSearchByKeyword(String keyword) {
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("query",keyword); // 필수값

        try {
            Mono<SearchResponse> res=  webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/v2/local/search/keyword.json")
                            .queryParams(map)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError,
                            clientResponse -> {
                                return clientResponse.bodyToMono(String.class)
                                    .flatMap(errorResponseBody -> {
                                        log.error("errorResponseBody = {}",errorResponseBody);
                                        if (clientResponse.rawStatusCode()==BAD_REQUEST.value()) {
                                            return Mono.error(new ServiceException(errorResponseBody, clientResponse.rawStatusCode()));
                                        } else {
                                            return Mono.error(new Throwable(errorResponseBody));
                                        }
                                        //레퍼런스에 나와있는 내용으로는 code -1 값일때는 재시도를 하여 해결한다.
                                        //https://developers.kakao.com/docs/latest/ko/reference/rest-api-reference#response-code
                                        //레퍼런스에 나와있는 response 정의와 실제 응답된 내용이 상이한듯 하다.. 확인 필요.
                                        //실제 : {"errorType":"MissingParameter","message":"query parameter required"}
                                        //레퍼런스 : { "code":-401,"msg":"InvalidTokenException"}
                                        /*
                                        try {
                                            KakaoException kakaoException = objectMapper.readValue(errorResponseBody, KakaoException.class);
                                            if (kakaoException.getCode().intValue() == -1) {
                                                return Mono.error(new ServiceException(kakaoException.getMsg(), clientResponse.rawStatusCode()));
                                            } else {
                                                return Mono.error(new Throwable(kakaoException.getMsg()));
                                            }
                                        } catch (JsonProcessingException e) {
                                            throw new RuntimeException(e);
                                        } */
                                    });
                    })
                    .bodyToMono(SearchResponse.class);
                    /*.retryWhen(Retry.backoff(1, Duration.ofSeconds(2))
                            .filter(throwable -> throwable instanceof ServiceException)
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                                log.error("External Service failed to process after max retries");
                                throw new ServiceException(SERVICE_UNAVAILABLE.name(),SERVICE_UNAVAILABLE.value());
                    }));*/

            return res.block();
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return SearchResponse.builder().build();
        }


    }
}
