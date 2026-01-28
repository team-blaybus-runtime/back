package com.init.domain.business.user.service;

import com.init.domain.business.common.service.EntitySimpReadService;
import com.init.domain.persistence.user.entity.User;
import com.init.infra.security.authentication.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final EntitySimpReadService entitySimpReadService;

    @Override
    @Cacheable(value = "securityUser", key = "#userId", unless = "#result == null")
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = entitySimpReadService.findUser(Long.parseLong(userId));
        return SecurityUserDetails.from(user);
    }
}
