package com.init.domain.persistence.user.entity;

import com.init.domain.persistence.common.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username;
    @ColumnDefault("NULL")
    private String password;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Role role;

    public static User of(String nickname, String username, String password, Role role) {
        User user = new User();
        user.nickname = nickname;
        user.username = username;
        user.password = password;
        user.role = role;
        return user;
    }

    public static User of(Role role) {
        User user = new User();
        user.role = role;
        return user;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
