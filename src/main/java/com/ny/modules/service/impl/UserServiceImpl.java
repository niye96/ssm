package com.ny.modules.service.impl;

import com.ny.common.service.impl.BaseServiceImpl;
import com.ny.common.utils.JsonResult;
import com.ny.modules.dao.UserMapper;
import com.ny.modules.entity.User;
import com.ny.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, UserMapper> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public JsonResult deleteUsers(List<String> userIds) {
        User user = new User();
        for (String userId : userIds) {
            user.setUserId(userId);
            userMapper.delete(user);
        }
        return new JsonResult("", true);
    }
}