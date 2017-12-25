package com.ny.common.dao;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T extends Serializable> {

    @SelectProvider(type = BaseProvider.class, method = "get")
    List<T> get(T entity);

    @InsertProvider(type = BaseProvider.class, method = "insert")
    int insert(T entity);

    @UpdateProvider(type = BaseProvider.class, method = "update")
    int update(T entity);

    @DeleteProvider(type = BaseProvider.class, method = "delete")
    int delete(T entity);
}
