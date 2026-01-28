package com.init.infra.oauth.client;

import com.init.infra.oauth.dto.OauthTokenRes;
import org.springframework.util.MultiValueMap;

public interface OauthClient {
    OauthTokenRes getIdToken(MultiValueMap<String, ?> body);
}
