package com.rongcapital.wallet.util.bean;

import com.google.common.collect.Maps;
import org.springframework.beans.FatalBeanException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class BeanUtil {

    /**
     * 获取一个bean里的属性为**的值集合
     */
    public static Set<Object> getValsOfBeanList(List beanList, String paramName) {
        Set<Object> valSet = new HashSet<Object>();
        paramName = paramName.toUpperCase();
        for (Object bean : beanList) {
            Class type = bean.getClass();
            BeanInfo beanInfo = null;
            try {
                beanInfo = Introspector.getBeanInfo(type);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            if (null != beanInfo) {
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (int i = 0; i < propertyDescriptors.length; i++) {
                    PropertyDescriptor descriptor = propertyDescriptors[i];
                    String propertyName = descriptor.getName().toUpperCase();
                    if (propertyName.equals(paramName)) {
                        Method readMethod = descriptor.getReadMethod();
                        Object val = null;
                        try {
                            val = readMethod.invoke(bean, new Object[0]);
                            if (val != null && !"".equals(val)) {
                                valSet.add(val);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return valSet;
    }

    /**
     * 获取传入的对象集合属性值
     * 
     * @param list
     * @param needPros 需要获取的字段,如果要获取所有传null
     * @return 返回不同对象的值用"||"隔开
     */
    public static String getProListVal(List list, String[] needPros) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Object obj : list) {
            sb.append(getProVal(obj, needPros)).append("||");
        }
        return sb.toString();
    }

    /**
     * 获取传入的对象属性值
     * 
     * @param bean
     * @param needPros 需要获取的字段,如果要获取所有传null
     * @return 返回的值用","隔开
     */
    public static String getProVal(Object bean, String[] needPros) {
        if (bean == null) {
            return "";
        }
        @SuppressWarnings("rawtypes")
        Class cls = bean.getClass();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(cls);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        if(null==beanInfo){
            return "";
        }
        // 获取字段的类型
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (propertyName.equals("class")) {
                continue;
            }
            if (needPros == null || (needPros != null && Arrays.asList(needPros).contains(propertyName))) {
                Method readMethod = descriptor.getReadMethod();
                Object result = null;
                try {
                    result = readMethod.invoke(bean, new Object[0]);
                    if (result instanceof Object[] && result != null) {
                        Object[] array = (Object[]) result;
                        result = Arrays.toString(array);
                    }
                    sb.append(propertyName + "=" + result + ",");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        String result = sb.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 校验一个对象传入的若干字段值是否有为null，或""的（不适用于基本类型） ，如果有为null的直接返回这个字段，否则返回null;
     * 
     * @param list
     * @param needPros 传null校验所有
     * @return
     */
    public static String validateBeanListProEmpty(List list, String[] needPros) {
        String result = null;
        for (Object bean : list) {
            result = validateProEmpty(bean, needPros);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 校验一个对象传入的若干字段值是否有为null，或""的（不适用于基本类型） ，如果有为null的直接返回这个字段，否则返回null;
     * 
     * @param bean
     * @param needPros 传null校验所有
     * @return
     */
    public static String validateProEmpty(Object bean, String[] needPros) {
        @SuppressWarnings("rawtypes")
        Class type = bean.getClass();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        if(null==beanInfo){
            return "";
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (propertyName.equals("class")) {
                continue;
            }
            if (needPros == null || (needPros != null && Arrays.asList(needPros).contains(propertyName))) {
                Method readMethod = descriptor.getReadMethod();
                Object result = null;
                try {
                    result = readMethod.invoke(bean, new Object[0]);
                    if (result == null || "".equals(result)) {
                        return propertyName;
                    }
                    if (result instanceof Object[]) {
                        Object[] array = (Object[]) result;
                        if (array.length == 0) {
                            return propertyName;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static <T> T maptobean(Class<? extends T> cls, Map<String, String[]> m) {
        Collection<String> c = new ArrayList<String>();
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            c.add(field.getName().toLowerCase());
        }
        Map<String, Object> _m = Maps.newHashMap();
        for (Map.Entry<String, String[]> entry : m.entrySet()) {
            if (c.contains(entry.getKey())) {
                String[] value = entry.getValue();
                if (value != null && value.length != 0) {
                    String v = value[0];
                    _m.put(entry.getKey(), v);
                }
            }
        }
        return convertMap2Bean(cls, _m);
    }

    public static final <T> T convertMap2Bean(Class<T> clazz, Map<String, Object> map) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return null;
        }

        if(null==beanInfo){
            return null;
        }
        PropertyDescriptor[] targetPds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && map.containsKey(targetPd.getName().toLowerCase())) {
                try {
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    writeMethod.invoke(t, map.get(targetPd.getName().toLowerCase()));
                } catch (Throwable ex) {
                    throw new FatalBeanException("Could not write property '" + targetPd.getName() + "' from bean", ex);
                }
            }
        }

        return t;
    }

    public static void main(String[] args) {
    }
}
