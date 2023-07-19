package com.local.openapi.configurations.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestClientConfigurationProperties {
    private String host;
    private int sessionDuration;
    private int connectTimeout;
    private long readTimeout;
    private long writeTimeout;
    private long responseTimeout;
    private String xForwardFor;
}
