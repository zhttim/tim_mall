package com.tim.gulimall.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tim
 * @date 2022/11/9 19:01
 **/
@ConfigurationProperties(prefix = "gulimall.oauth")
@Component
@Data
public class Oauth2ConfigProperties {
    private String clientId;
    private String clientSecret;
    private String grantType;
}
