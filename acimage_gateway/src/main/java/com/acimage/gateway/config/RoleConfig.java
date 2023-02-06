package com.acimage.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "role")
public class RoleConfig {
    private int visitorId;
    private int userId;
}
