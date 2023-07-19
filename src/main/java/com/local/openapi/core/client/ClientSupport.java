package com.local.openapi.core.client;

import com.local.openapi.configurations.properties.RestClientConfigurationProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


abstract class ClientSupport {

    WebClient webClient;

    ClientSupport(RestClientConfigurationProperties config) {

        ConnectionProvider connectionProvider = ConnectionProvider.builder("webclient-provider")
                .maxConnections(32)
                .maxIdleTime(Duration.ofSeconds(5))
                .maxLifeTime(Duration.ofSeconds(5))
                .pendingAcquireTimeout(Duration.ofMillis(5000))
                .pendingAcquireMaxCount(-1)
                .evictInBackground(Duration.ofSeconds(30))
                .lifo()
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout())
                .baseUrl(config.getHost())
                .disableRetry(true)
                .headers(httpHeaders(config))
                .responseTimeout(Duration.ofMillis(config.getResponseTimeout()))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(config.getReadTimeout(), TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(config.getWriteTimeout(), TimeUnit.MILLISECONDS)));


        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();

    }

    protected Consumer<HttpHeaders> httpHeaders(RestClientConfigurationProperties config) {
        return headers -> {

        };
    }

}
