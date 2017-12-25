package com.ny.modules.web;

import com.ny.common.controller.BaseController;
import com.ny.common.utils.JsonResult;
import com.ny.modules.entity.User;
import com.ny.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController<User, UserService> {

    @Autowired
    private UserService userService;

    @Override
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public JsonResult getList(@RequestBody User user,
                              @RequestParam(defaultValue = "1") Integer start,
                              @RequestParam(defaultValue = "30") Integer pageSize) {
        return userService.getListByNoSql(user, start, pageSize);
    }

    @Override
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public JsonResult insert(@Valid @RequestBody User user, BindingResult result) {
        JsonResult res = checkResult(result);
        if (res != null)
            return res;
        res = userService.insertByNoSql(user);
        return res;
    }

    @Override
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JsonResult delete(@RequestBody User user) {
        return userService.deleteByNoSql(user);
    }

    @RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
    public JsonResult deleteUsers(@RequestParam List<String> userIds) {
        return userService.deleteUsers(userIds);
    }

    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult update(@Valid @RequestBody User user, BindingResult result) {
        JsonResult res = checkResult(result);
        if (res != null)
            return res;
        res = userService.updateByNoSql(user);
        return res;
    }
}
