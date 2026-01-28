package com.init.infra.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OidcPublicKeyRes {
    List<OidcPublicKey> keys;
}
