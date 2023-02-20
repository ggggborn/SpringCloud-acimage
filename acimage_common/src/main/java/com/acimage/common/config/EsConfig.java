package com.acimage.common.config;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;


@Profile(value = {"test","dev","dev2","server"})
@Configuration
@ConditionalOnClass(RestHighLevelClient.class)
public class EsConfig {

    @Bean
    public RestHighLevelClient devRestHighLevelClient(@Autowired RestClientBuilder restClientBuilder) {

        return new RestHighLevelClient(restClientBuilder.setHttpClientConfigCallback(requestConfig -> {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            return requestConfig.setDefaultCredentialsProvider(credentialsProvider);
        }));
    }

}
