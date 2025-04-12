package com.vuog.core.module.auth.application.service.impl;

import com.vuog.core.common.exception.DataNotFoundException;
import com.vuog.core.common.exception.UnAuthorizationException;
import com.vuog.core.common.util.Context;
import com.vuog.core.module.auth.application.dto.CreateUserSettingReq;
import com.vuog.core.module.auth.application.service.UserSettingService;
import com.vuog.core.module.auth.domain.model.User;
import com.vuog.core.module.auth.domain.model.UserSetting;
import com.vuog.core.module.auth.domain.repository.UserSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserSettingServiceImpl implements UserSettingService {

    private final UserSettingRepository userSettingRepository;

    public UserSettingServiceImpl(UserSettingRepository userSettingRepository) {
        this.userSettingRepository = userSettingRepository;
    }


    @Override
    public List<UserSetting> findAllByUser(User user) {
        return userSettingRepository.findAllByUser(user);
    }

    @Override
    public UserSetting getByKey(String key) {
        User user = Context.getUser();

        Optional<UserSetting> userSettingOpt = userSettingRepository.findByKey(key);

        if (userSettingOpt.isPresent()) {
            UserSetting userSetting = userSettingOpt.get();
            if (!userSetting.getUser().getId().equals(user.getId())) {
                throw new UnAuthorizationException("User not authenticated");
            }

            return userSetting;
        }

        throw new DataNotFoundException("Not found user setting with key: " + key);
    }

    @Override
    public UserSetting create(CreateUserSettingReq req) {
        UserSetting userSetting = new UserSetting();
        userSetting.setKey(req.getKey());
        userSetting.setValue(req.getValue());

        userSetting.setUser(Context.getUser());
        return userSettingRepository.save(userSetting);
    }

    @Override
    public UserSetting updateSetting(String key, String value) {
        UserSetting userSetting = getByKey(key);

        if (Objects.nonNull(userSetting)) {
            userSetting.setValue(value);
            return userSettingRepository.save(userSetting);
        }

        throw new DataNotFoundException("Not found setting with key: " + key);
    }


}
