package com.init.global.config;

import com.init.infra.oauth.properties.GoogleOidcProperties;
import com.init.infra.oauth.properties.KakaoOidcProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        ServerProperties.class,
        GoogleOidcProperties.class,
        KakaoOidcProperties.class
})
public class OauthConfig {
}
