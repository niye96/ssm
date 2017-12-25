package com.ny.common.service;

import com.ny.common.utils.JsonResult;

import java.io.Serializable;

public interface BaseService<T extends Serializable> {
    /**
     * 通过数据库分页查询
     *
     * @param entity
     * @param start    起始页
     * @param pageSize 页面大小
     * @return
     * @throws Exception
     */
    JsonResult getList(T entity, int start, int pageSize);

    /**
     * 通过数据库查询全部数据
     *
     * @param entity
     * @return
     * @throws Exception
     */
    JsonResult getAllList(T entity);

    /**
     * 通过主键查询
     *
     * @param entity
     * @return
     */
    JsonResult get(T entity);

    /**
     * 插入
     *
     * @param entity
     * @return
     */
    JsonResult insert(T entity);

    /**
     * 通过对象的主键更新
     *
     * @param entity
     * @return
     */
    JsonResult update(T entity);

    /**
     * 通过对象的主键删除
     *
     * @param entity
     * @return
     */
    JsonResult delete(T entity);

    /**
     * 通过缓存分页查询
     *
     * @param entity
     * @param start    起始页
     * @param pageSize 页面大小
     * @return
     * @throws Exception
     */
    JsonResult getListByNoSql(T entity, int start, int pageSize);

    /**
     * 查缓存
     *
     * @param entity
     * @return
     * @throws Exception
     */
    JsonResult getByNoSql(T entity);

    /**
     * 插入并更新缓存
     *
     * @param entity
     * @return
     * @throws Exception
     */
    JsonResult insertByNoSql(T entity);

    /**
     * 更新数据库并更新缓存
     *
     * @param entity
     * @return
     * @throws Exception
     */
    JsonResult updateByNoSql(T entity);

    /**
     * 删除并更新缓存
     *
     * @param entity
     * @return
     * @throws Exception
     */
    JsonResult deleteByNoSql(T entity);
}
