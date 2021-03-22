package com.ayush.discussionforum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
public class AppConfig {

    @Value("${app.url}")
    private String appUrl;

    public String getAppUrl(){
        return appUrl;
    }
}
