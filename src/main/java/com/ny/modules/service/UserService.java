package com.ny.modules.service;

import com.ny.common.service.BaseService;
import com.ny.common.utils.JsonResult;
import com.ny.modules.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService extends BaseService<User> {

    @Transactional
    JsonResult deleteUsers(List<String> userIds);

}
