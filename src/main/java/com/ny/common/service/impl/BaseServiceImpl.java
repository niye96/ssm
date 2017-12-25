package com.ny.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ny.common.dao.BaseMapper;
import com.ny.common.service.BaseService;
import com.ny.common.utils.DtoUtil;
import com.ny.common.utils.JsonResult;
import com.ny.common.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

public class BaseServiceImpl<T extends Serializable, D extends BaseMapper> implements BaseService<T> {
    @Autowired
    public D mapper;
    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    public JsonResult getList(T entity, int start, int pageSize) {
        PageHelper.startPage(start, pageSize);
        List<T> list = mapper.get(entity);
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        Long total = pageInfo.getTotal();
        return new JsonResult(total, list);
    }

    public JsonResult getAllList(T entity) {
        List<T> list = mapper.get(entity);
        long total = list.size();
        return new JsonResult(total, list);
    }

    public JsonResult get(T entity) {
        List<T> list = mapper.get(entity);
        if (list.size() > 0) {
            return new JsonResult(0, list.get(0));
        }
        return new JsonResult("", false);
    }

    @Transactional
    public JsonResult insert(T entity) {
        //采用UUID不会产生重复的键
        //通过UUID主键生成策略
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        DtoUtil.setId(entity, id);
        int i = mapper.insert(entity);
        if (i == 0) {
            return new JsonResult("", false);
        } else {
            return new JsonResult("", true);
        }
    }

    @Transactional
    public JsonResult update(T entity) {
        int i = mapper.update(entity);
        if (i == 0) {
            return new JsonResult("", false);
        } else {
            return new JsonResult("", true);
        }
    }

    @Transactional
    public JsonResult delete(T entity) {
        int i = mapper.delete(entity);
        if (i == 0) {
            return new JsonResult("", false);
        } else {
            return new JsonResult("", true);
        }
    }

    public JsonResult getListByNoSql(T entity, int start, int pageSize) {
        String clazzName = entity.getClass().getSimpleName().toUpperCase();
        List<T> list = new ArrayList<T>();
        //查询redis
        Set<String> ids = redisTemplate.opsForZSet().reverseRange(clazzName + "_INFO_LIST",
                (start - 1) * pageSize, start * pageSize - 1);
        if (!ids.isEmpty()) {
            for (String id : ids) {
                String s = redisTemplate.opsForValue().get(clazzName + "_INFO:" + id);
                list.add((T) JsonUtils.jsonToPojo(s, entity.getClass()));
            }
            long total = Long.parseLong(redisTemplate.opsForValue().get(clazzName + "_INFO:TOTAL"));
            return new JsonResult(total, "success", true, list);
        }
        //查询数据库
        JsonResult res = getAllList(entity);
        list = (List<T>) res.getData();
        List<T> temp = new ArrayList<T>();
        if (start * pageSize <= list.size()) {
            for (int i = (start - 1) * pageSize, j = start * pageSize; i < j; i++) {
                temp.add(list.get(i));
            }
        } else {
            for (int i = (start - 1) * pageSize, j = list.size(); i < j; i++) {
                temp.add(list.get(i));
            }
        }
        //写入redis
        redisTemplate.opsForValue().set(clazzName + "_INFO:TOTAL", Integer.toString(list.size()));
        for (T t : list) {
            redisTemplate.opsForZSet().add(clazzName + "_INFO_LIST", DtoUtil.getId(t).toString(), new Date().getTime());
            redisTemplate.opsForValue().set(clazzName + "_INFO:" + DtoUtil.getId(t).toString(), JsonUtils.objectToJson(t));
        }
        return new JsonResult(list.size(), temp);
    }

    public JsonResult getByNoSql(T entity) {
        String clazzName = entity.getClass().getSimpleName().toUpperCase();
        //先从缓存中读取
        String info = redisTemplate.opsForValue().get(clazzName + "_INFO:" + DtoUtil.getId(entity).toString());
        if (info != null) {
            T res = (T) JsonUtils.jsonToPojo(info, entity.getClass());
            return new JsonResult(0, res);
        }
        //查数据库
        JsonResult res = get(entity);
        //如果不为空，更新缓存
        if (res.getData() != null) {
            String id = DtoUtil.getId(entity).toString().toUpperCase();
            redisTemplate.opsForZSet().add(clazzName + "_INFO_LIST", id, new Date().getTime());
            redisTemplate.opsForValue().set(clazzName + "_INFO:" + id, JsonUtils.objectToJson(res.getData()));
            String tempTotal = redisTemplate.opsForValue().get(clazzName + "_INFO:TOTAL");
            long total = 0;
            if (!StringUtils.isEmpty(tempTotal))
                total = Long.parseLong(tempTotal);
            redisTemplate.opsForValue().set(clazzName + "_INFO:TOTAL", Long.toString(total + 1));
            return new JsonResult(0, res);
        }
        return new JsonResult("未找到", false);
    }

    @Transactional
    public JsonResult insertByNoSql(T entity) {
        String clazzName = entity.getClass().getSimpleName().toUpperCase();
        //通过UUID策略生成id
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        DtoUtil.setId(entity, id);
        int i = mapper.insert(entity);
        if (i == 0) {
            return new JsonResult("", false);
        }
        //写入redis中
        redisTemplate.opsForValue().set(clazzName + "_INFO:" + id, JsonUtils.objectToJson(entity));
        String tempTotal = redisTemplate.opsForValue().get(clazzName + "_INFO:TOTAL");
        long total = 0;
        if (!StringUtils.isEmpty(tempTotal))
            total = Long.parseLong(tempTotal);
        redisTemplate.opsForValue().set(clazzName + "_INFO:TOTAL", Long.toString(total + 1));
        redisTemplate.opsForZSet().add(clazzName + "_INFO_LIST", id, new Date().getTime());
        return new JsonResult("", true);
    }

    @Transactional
    public JsonResult updateByNoSql(T entity) {
        String clazzName = entity.getClass().getSimpleName().toUpperCase();
        String id = DtoUtil.getId(entity).toString();
        JsonResult res = update(entity);
        //写入redis
        redisTemplate.opsForValue().set(clazzName + "_INFO:" + id, JsonUtils.objectToJson(entity));
        redisTemplate.opsForZSet().add(clazzName + "_INFO_LIST", id, new Date().getTime());
        return res;
    }

    @Transactional
    public JsonResult deleteByNoSql(T entity) {
        String id = DtoUtil.getId(entity).toString();
        String clazzName = entity.getClass().getSimpleName().toUpperCase();
        JsonResult res = delete(entity);
        redisTemplate.delete(clazzName + "_INFO:" + id);
        redisTemplate.opsForZSet().remove(clazzName + "_INFO_LIST", id);
        String tempTotal = redisTemplate.opsForValue().get(clazzName + "_INFO:TOTAL");
        long total = 0;
        if (!StringUtils.isEmpty(tempTotal))
            total = Long.parseLong(tempTotal);
        if (total != 0)
            redisTemplate.opsForValue().set(clazzName + "_INFO:TOTAL", Long.toString(total - 1));
        return res;
    }

}
