package com.demi.utils;

import java.lang.reflect.Field;

/**
 * 工具类之属性反射赋值模板
 */
public class BeanUtil {
    public static void fieldTemplate(Object origin, Object dest) {
        try {
            // 判断两个对象是否是统一类型
            if (origin.getClass() != dest.getClass()) {
                // 不是同一类型，抛出异常
                throw new RuntimeException("两个对象必须是同一类型");
            }
            Class<?> oClass = origin.getClass();
            // 获取origin的所有属性
            Field[] fields = oClass.getDeclaredFields();
            // 遍历所有的属性
            for (Field field : fields) {
                // 排除序列版本号
                if ("serialVersionUID".equals(field.getName()))
                    continue;
                // 打破封装
                field.setAccessible(true);
                // 赋值操作
                // field.get(dest) : 从dest对象中取出和当前属性field相同的属性的值返回赋值给origin。
                field.set(origin, field.get(dest));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
