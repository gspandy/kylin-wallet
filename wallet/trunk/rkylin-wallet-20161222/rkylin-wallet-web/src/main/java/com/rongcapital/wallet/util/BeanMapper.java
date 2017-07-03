package com.rongcapital.wallet.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * 
 * 1. 持有Mapper的单例. 2. 返回值类型转换. 3. 批量转换Collection中的所有对象. 4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 * 
 */
public class BeanMapper {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(final Object source, final Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    @SuppressWarnings("rawtypes")
    public static <T> List<T> mapList(final Collection sourceList, final Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     */
    public static void copy(final Object source, final Object destinationObject) {
        dozer.map(source, destinationObject);
    }

    public static <T> Map<String, T> toMap(final Object target) {
        return toMap(target, false);
    }

    /**
     * 将目标对象的所有属性转换成Map对象
     * 
     * @param target 目标对象
     * @param ignoreParent 是否忽略父类的属性
     * 
     * @return Map
     */
    public static <T> Map<String, T> toMap(final Object target, final boolean ignoreParent) {
        return toMap(target, ignoreParent, false);
    }

    /**
     * 将目标对象的所有属性转换成Map对象
     * 
     * @param target 目标对象
     * @param ignoreParent 是否忽略父类的属性
     * @param ignoreEmptyValue 是否不把空值添加到Map中
     * 
     * @return Map
     */
    public static <T> Map<String, T> toMap(final Object target, final boolean ignoreParent,
            final boolean ignoreEmptyValue) {
        return toMap(target, ignoreParent, ignoreEmptyValue, new String[0]);
    }

    /**
     * 将目标对象的所有属性转换成Map对象
     * 
     * @param target 目标对象
     * @param ignoreParent 是否忽略父类的属性
     * @param ignoreEmptyValue 是否不把空值添加到Map中
     * @param ignoreProperties 不需要添加到Map的属性名
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> toMap(final Object target, final boolean ignoreParent,
            final boolean ignoreEmptyValue, final String...ignoreProperties) {
        Map<String, T> map = new HashMap<String, T>();

        List<Field> fields = getAccessibleFields(target.getClass(), ignoreParent);

        for (Iterator<Field> it = fields.iterator(); it.hasNext();) {
            Field field = it.next();
            T value = null;

            try {
                value = (T) field.get(target);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (ignoreEmptyValue && (((value == null) || value.toString().equals(""))
                    || ((value instanceof Collection) && ((Collection<?>) value).isEmpty())
                    || ((value instanceof Map) && ((Map<?, ?>) value).isEmpty()))) {
                continue;
            }

            boolean flag = true;
            String key = field.getName();

            for (String ignoreProperty : ignoreProperties) {
                if (key.equals(ignoreProperty)) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                map.put(key, value);
            }
        }

        return map;
    }

    @SuppressWarnings("rawtypes")
    private static List<Field> getAccessibleFields(final Class targetClass, final boolean ignoreParent) {

        List<Field> fields = new ArrayList<Field>();

        Class<?> sc = targetClass;

        do {
            Field[] result = sc.getDeclaredFields();

            if (!ArrayUtils.isEmpty(result)) {

                for (Field field : result) {
                    field.setAccessible(true);
                }

                CollectionUtils.addAll(fields, result);
            }

            sc = sc.getSuperclass();

        } while ((sc != Object.class) && !ignoreParent);

        return fields;
    }
}
