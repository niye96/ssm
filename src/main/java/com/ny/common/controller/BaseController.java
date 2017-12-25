package com.ny.common.controller;

import com.ny.common.service.BaseService;
import com.ny.common.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class BaseController<T extends Serializable, D extends BaseService> {
    @Autowired
    protected D service;

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public JsonResult getList(@RequestBody T t,
                              @RequestParam(defaultValue = "1") Integer start,
                              @RequestParam(defaultValue = "30") Integer pageSize) {
        JsonResult res = service.getList(t, start, pageSize);
        return res;
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public JsonResult insert(@Valid @RequestBody T t, BindingResult result) {
        JsonResult res;
        res = checkResult(result);
        if (res != null) return res;
        res = service.insert(t);
        return res;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public JsonResult delete(@RequestBody T t) {
        return service.delete(t);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public JsonResult update(@Valid @RequestBody T t, BindingResult result) {
        JsonResult res;
        res = checkResult(result);
        if (res != null) return res;
        res = service.update(t);
        return res;
    }

    public JsonResult checkResult(BindingResult result) {
        if (result.hasErrors()) {
            String msg = "";
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                msg += fieldError.getDefaultMessage() + ",";
            }
            return new JsonResult(msg.substring(0, msg.length() - 1), false);
        }
        return null;
    }
}
