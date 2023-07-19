package com.local.openapi.core.client;

import com.local.openapi.configurations.properties.NaverClientConfigurationProperties;
import com.local.openapi.configurations.properties.RestClientConfigurationProperties;
import com.local.openapi.core.exception.ServiceException;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
public class NaverClientImpl extends ClientSupport implements NaverClient{
    private NaverClientConfigurationProperties config;

    public NaverClientImpl(NaverClientConfigurationProperties config) {
        super(config);
        this.config = config;
    }
    @Override
    protected Consumer<HttpHeaders> httpHeaders(RestClientConfigurationProperties config) {
        NaverClientConfigurationProperties configurationProperties = ((NaverClientConfigurationProperties) config);
        return headers -> {
            headers.add("X-Naver-Client-Id", configurationProperties.getId());
            headers.add("X-Naver-Client-Secret", configurationProperties.getSecret());
        };
    }

    @Override
    public SearchResponse localSearchByKeyword(String keyword) {

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("query",keyword); // 필수값
        map.add("display","5");

        try {
            Mono<SearchResponse> res=  webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/v1/search/local.json")
                            .queryParams(map)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError,
                            clientResponse -> {
                                return clientResponse.bodyToMono(String.class)
                                        .flatMap(errorResponseBody -> {
                                            return Mono.error(new Throwable(errorResponseBody));
                                        });
                            })
                    .bodyToMono(SearchResponse.class);

            return res.block();

        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return SearchResponse.builder().build();
        }
    }
}
