package com.rongcapital.wallet.util.collection;

import java.util.Map;

/**
 * 集合工具类
 * Description:
 * @author: Achilles
 * @CreateDate: 2016年3月3日
 * @version: V1.0
 */
public class CollectionUtil<T> {
    
    public static void main(String[] args) {
        System.out.println(getArrayOneVal(new String[]{"ndsandoi"}));
    }
    
    /**
     * 获取map的键值(rop输入参数的打印)
     * @param paramMap
     * @return
     */
    public static String getMapKeyVal(Map<String, String[]> paramMap){
    	if (paramMap==null||paramMap.size()==0) {
			return "";
		}
		StringBuffer paramsBuffer = new StringBuffer();
		for (Object keyObj : paramMap.keySet().toArray()) {
			String[] strs = paramMap.get(keyObj);
			if (strs == null) {
				continue;
			}
			for (String value : strs) {
				paramsBuffer.append(keyObj + "=" + value + ", ");
			}
		}
		return paramsBuffer.toString();
    }
    
    /**
     * 获取一个数组的第一位的值
     * @param array
     * @return String
     * @author Achilles
     * @since 2016年3月3日
     */
    public static <T> T getArrayOneVal(T[] array){
        if (array==null||array.length==0) {
            return null; 
        }
        return array[0];
    }
}
