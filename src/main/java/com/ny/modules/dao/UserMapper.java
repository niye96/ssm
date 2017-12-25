package com.ny.modules.dao;

import com.ny.common.dao.BaseMapper;
import com.ny.modules.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
