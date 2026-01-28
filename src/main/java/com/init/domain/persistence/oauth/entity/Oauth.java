package com.init.domain.persistence.oauth.entity;

import com.init.domain.persistence.common.model.DateAuditable;
import com.init.domain.persistence.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Oauth extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;
    @NotNull
    private String sub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Oauth of(OauthProvider provider, String oauthId, User user) {
        Oauth oauth = new Oauth();
        oauth.oauthProvider = provider;
        oauth.sub = oauthId;
        oauth.user = user;
        return oauth;
    }
}
