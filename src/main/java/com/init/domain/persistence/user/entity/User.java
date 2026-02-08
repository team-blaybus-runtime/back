package com.init.domain.persistence.user.entity;

import com.init.domain.persistence.common.model.DateAuditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
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
    @Comment("전공")
    private String major;
    @Comment("학년")
    private Integer grade;
    @Comment("목표")
    private String goal;
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public static User of(String username, String password, Role role) {
        User user = new User();
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

    public void updateProfile(String nickname, String major, Integer grade, String goal) {
        this.nickname = nickname;
        this.major = major;
        this.grade = grade;
        this.goal = goal;
    }
}
