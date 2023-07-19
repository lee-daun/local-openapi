package com.local.openapi.configurations.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverClientConfigurationProperties extends RestClientConfigurationProperties{
    private String id;
    private String secret;

}
