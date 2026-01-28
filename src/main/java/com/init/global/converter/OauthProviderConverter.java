package com.init.global.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.init.domain.persistence.oauth.entity.OauthProvider;

@Component
public class OauthProviderConverter implements Converter<String, OauthProvider> {
    @Override
    public OauthProvider convert(String source) {
        return OauthProvider.valueOf(source.toUpperCase());
    }
}
