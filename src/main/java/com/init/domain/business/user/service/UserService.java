package com.init.domain.business.user.service;


import com.init.application.dto.user.req.NicknameCheckReq;
import com.init.application.dto.user.req.NicknameUpdateReq;
import com.init.application.dto.user.req.ProfileUpdateReq;
import com.init.application.dto.user.res.UserDetailRes;
import com.init.application.mapper.UserMapper;
import com.init.domain.business.common.service.EntitySimpReadService;
import com.init.domain.business.user.error.UserErrorCode;
import com.init.domain.persistence.user.entity.Role;
import com.init.domain.persistence.user.entity.User;
import com.init.domain.persistence.user.repository.UserRepository;
import com.init.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final EntitySimpReadService entitySimpReadService;
    private final UserRepository userRepository;

    @Transactional
    public User saveGuest() {
        return userRepository.save(User.of(Role.GUEST));
    }

    @Transactional
    public User saveUserWithEncryptedPassword(String username, String encryptedPassword) {
        if (userRepository.existsByUsername(username))
            throw new GlobalException(UserErrorCode.CONFLICT_USERNAME);

        User user = User.of(username, encryptedPassword, Role.GUEST);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserDetailRes readUserDetail(Long userId) {
        User user = entitySimpReadService.findUser(userId);
        return UserMapper.toDetailRes(user);
    }

    @Transactional(readOnly = true)
    public List<UserDetailRes> readUserDetails() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDetailRes)
                .toList();
    }

    @Transactional(readOnly = true)
    public User readUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND));
    }

    @Transactional
    public void updateNickname(Long userId, NicknameUpdateReq req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND));

        validateNicknameDuplication(req.nickname());

        user.updateNickname(req.nickname());
    }

    @Transactional(readOnly = true)
    public void validateNicknameDuplication(String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new GlobalException(UserErrorCode.CONFLICT_USERNAME);
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedNickname(NicknameCheckReq req) {
        return userRepository.existsByNickname(req.nickname());
    }

    @Transactional
    public void updateProfile(Long userId, ProfileUpdateReq req) {
        User user = entitySimpReadService.findUser(userId);
        user.updateProfileAndSetRoleUser(
                req.nickname(), req.major(), req.grade(), req.goal()
        );
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
