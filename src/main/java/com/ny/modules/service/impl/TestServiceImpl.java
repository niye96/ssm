package com.ny.modules.service.impl;

import com.ny.common.service.impl.BaseServiceImpl;
import com.ny.modules.dao.TestMapper;
import com.ny.modules.entity.Test;
import com.ny.modules.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends BaseServiceImpl<Test, TestMapper> implements TestService {

}
