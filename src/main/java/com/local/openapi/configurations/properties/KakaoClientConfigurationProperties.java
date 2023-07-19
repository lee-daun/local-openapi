package com.local.openapi.configurations.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoClientConfigurationProperties extends RestClientConfigurationProperties{
    private String apikey;
}
