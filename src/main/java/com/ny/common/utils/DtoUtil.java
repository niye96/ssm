package com.ny.common.utils;

import org.springframework.data.annotation.Id;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DtoUtil {
    public static String tableName(Object obj) {
        String className = obj.getClass().getSimpleName();
        return className.toLowerCase();
    }

    /**
     * 查询语句条件
     *
     * @param obj
     * @return 以Key-Value形式返回键值对
     */
    public static String getSelectParams(Object obj) {
        Class clazz = (Class) obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (fieldName.equals("serialVersionUID")) continue;
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (val != null && !val.equals("")) {
                s.append(fieldName + " = #{" + fieldName + "} AND ");
            }
        }
        if (s.length() != 0)
            s.delete(s.lastIndexOf("AND") - 1, s.length());
        return s.toString();
    }

    /**
     * 返回键值对
     *
     * @param obj
     * @return 以Key-Value形式返回键值对
     */
    public static Map<String, String> getInsertParams(Object obj) {
        Class clazz = (Class) obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (fieldName.equals("serialVersionUID")) continue;
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (val != null && !val.equals("")) {
                map.put(fieldName, "#{" + fieldName + "}");
            }
        }
        return map;
    }

    /**
     * 获取更新时的属性值
     *
     * @param obj: entity
     * @return 拼接的更新字符串
     */
    public static String getUpdateValues(Object obj) {
        Class clazz = (Class) obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getAnnotation(Id.class) != null) {                     //如果是主键则不放入map中
                continue;
            }
            String fieldName = field.getName();
            if (fieldName.equals("serialVersionUID")) continue;
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (val != null && !val.equals("")) {
                s.append(fieldName + "=#{" + fieldName + "},");
            }
        }
        if (s.length() != 0)
            s.deleteCharAt(s.length() - 1);
        return s.toString();
    }

    /**
     * 获取entity的主键
     *
     * @param obj
     * @return 主键的值
     */
    public static String id(Object obj) {
        Class clazz = (Class) obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer id = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (field.getAnnotation(Id.class) != null) {
                id.append(fieldName + " = #{" + fieldName + "} AND ");
            }
        }
        if (id.length() == 0)
            try {
                throw new Exception("类" + clazz.getSimpleName() + "未设置主键");
            } catch (Exception e) {
                e.printStackTrace();
            }
        id.delete(id.lastIndexOf("AND") - 1, id.length());
        return id.toString();
    }

    public static void setId(Object obj, Object val) {
        Class clazz = (Class) obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();
            if (field.getAnnotation(Id.class) != null) {
                try {
                    field.set(obj, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    public static Object getId(Object obj) {
        Class clazz = (Class) obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Object res = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String fieldName = field.getName();
            if (field.getAnnotation(Id.class) != null) {
                try {
                    res = field.get(obj);
                    return res;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}
