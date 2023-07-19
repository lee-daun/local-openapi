package com.local.openapi.configurations;

import com.local.openapi.configurations.properties.KakaoClientConfigurationProperties;
import com.local.openapi.configurations.properties.NaverClientConfigurationProperties;
import com.local.openapi.core.client.KakaoClient;
import com.local.openapi.core.client.KakaoClientImpl;
import com.local.openapi.core.client.NaverClient;
import com.local.openapi.core.client.NaverClientImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "application.web-client.kakao")
    KakaoClientConfigurationProperties kakaoConfig() {
        return new KakaoClientConfigurationProperties();
    }
    @Bean
    public KakaoClient kakaoClient() {
        return new KakaoClientImpl(kakaoConfig());
    }

    @Bean
    @ConfigurationProperties(prefix = "application.web-client.naver")
    NaverClientConfigurationProperties naverConfig() {

        return new NaverClientConfigurationProperties();
    }
    @Bean
    public NaverClient naverClient() {
        return new NaverClientImpl(naverConfig());
    }

}
