package com.ny.common.dao;

import com.ny.common.utils.DtoUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.io.Serializable;
import java.util.Map;

public class BaseProvider<T extends Serializable> {


    /**
     * 查询，通过非空的属性查询
     *
     * @param entity
     * @return
     */
    public String get(final T entity) {
        final String params = DtoUtil.getSelectParams(entity);
        return new SQL() {
            {
                SELECT("*");
                FROM(DtoUtil.tableName(entity));
                if (!StringUtils.isEmpty(params))
                    WHERE(params);
            }
        }.toString();
    }

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    public String insert(final T entity) {
        final Map<String, String> values = DtoUtil.getInsertParams(entity);
        return new SQL() {
            {
                INSERT_INTO(DtoUtil.tableName(entity));
                for (String key :
                        values.keySet()) {
                    VALUES(key, values.get(key));
                }
            }
        }.toString();
    }

    /**
     * 根据主键更新，更新对象中非空的属性
     *
     * @param entity
     * @return SQL语句
     */
    public String update(final T entity) {
        return new SQL() {
            {
                UPDATE(DtoUtil.tableName(entity));
                SET(DtoUtil.getUpdateValues(entity));
                WHERE(DtoUtil.id(entity));
            }
        }.toString();
    }

    /**
     * 根据主键删除
     *
     * @param entity
     * @return 返回SQL语句
     */
    public String delete(final T entity) {
        return new SQL() {
            {
                DELETE_FROM(DtoUtil.tableName(entity));
                WHERE(DtoUtil.id(entity));
            }
        }.toString();
    }

}
