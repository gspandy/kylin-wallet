/**
 * <p>Title: MyJacksonPropertyNamingStrategy.java</p>
 * <p>Description:rkylin-wallet-web </p>
 * <p>Copyright:2016年11月2日 </p>
 * <p>Company: rongshu</p>
 * <p>author: 磊</p>
 * <p>package: com.rongcapital.wallet.util</p>	
 * @version v1.0.0
 */
package com.rongcapital.wallet.util;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;
import com.google.common.base.CaseFormat;

/**
 * Description:
 * 
 * @author: 磊
 * @CreateDate: 2016年11月2日
 * @version: V1.0
 */
public class MyJacksonPropertyNamingStrategy extends PropertyNamingStrategyBase {

    /**
     * Description:
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public MyJacksonPropertyNamingStrategy() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase#translate(java.lang.String)
     */
    @Override
    public String translate(String input) {
        // 下划线转驼峰 user_name to userName
        String resultStr = null;

        if (!StringUtils.isEmpty(input) && ("_".indexOf(input) != -1)) {
            resultStr = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, input);
        }
        return resultStr;

    }
}
