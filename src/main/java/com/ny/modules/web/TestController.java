package com.ny.modules.web;

import com.ny.common.controller.BaseController;
import com.ny.modules.entity.Test;
import com.ny.modules.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("test")
@RestController
public class TestController extends BaseController<Test, TestService> {

}
