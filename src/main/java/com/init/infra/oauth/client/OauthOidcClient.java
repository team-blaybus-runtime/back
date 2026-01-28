package com.init.infra.oauth.client;


import com.init.infra.oauth.dto.OidcPublicKeyRes;

public interface OauthOidcClient extends OauthClient {
    OidcPublicKeyRes getOidcPublicKey();
}
