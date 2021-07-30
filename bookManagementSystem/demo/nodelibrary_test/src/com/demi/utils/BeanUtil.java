package com.demi.utils;

import java.lang.reflect.Field;

/**
 * ������֮���Է��丳ֵģ��
 */
public class BeanUtil {
    public static void fieldTemplate(Object origin, Object dest) {
        try {
            // �ж����������Ƿ���ͳһ����
            if (origin.getClass() != dest.getClass()) {
                // ����ͬһ���ͣ��׳��쳣
                throw new RuntimeException("�������������ͬһ����");
            }
            Class<?> oClass = origin.getClass();
            // ��ȡorigin����������
            Field[] fields = oClass.getDeclaredFields();
            // �������е�����
            for (Field field : fields) {
                // �ų����а汾��
                if ("serialVersionUID".equals(field.getName()))
                    continue;
                // ���Ʒ�װ
                field.setAccessible(true);
                // ��ֵ����
                // field.get(dest) : ��dest������ȡ���͵�ǰ����field��ͬ�����Ե�ֵ���ظ�ֵ��origin��
                field.set(origin, field.get(dest));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
