package com.init.application.mapper;


import com.init.application.dto.user.res.UserDetailRes;
import com.init.domain.persistence.user.entity.User;
import com.init.global.annotation.Mapper;

@Mapper
public final class UserMapper {
    public static UserDetailRes toDetailRes(User user) {
        return new UserDetailRes(
                user.getId(),
                user.getNickname(),
                user.getUsername(),
                user.getPassword(),
                user.getMajor(),
                user.getGrade(),
                user.getGoal(),
                user.getRole()
        );
    }
}
