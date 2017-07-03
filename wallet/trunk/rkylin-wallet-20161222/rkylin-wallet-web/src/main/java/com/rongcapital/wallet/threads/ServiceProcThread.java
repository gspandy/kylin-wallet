package com.rongcapital.wallet.threads;

import java.io.Serializable;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.rongcapital.wallet.api.po.UserLoginHistoryPo;
import com.rongcapital.wallet.api.service.LoginAipService;
import com.rongcapital.wallet.util.validate.ValidateUtils;




public class ServiceProcThread implements Callable<String> {
    
    private static Logger logger = LoggerFactory.getLogger(ServiceProcThread.class);
   
   
    private LoginAipService loginAipService;

    private UserLoginHistoryPo po;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ServiceProcThread() {

    }

    public ServiceProcThread(UserLoginHistoryPo po,LoginAipService loginAipService) {

        this.po = po;
        this.loginAipService=loginAipService;
    }

    public String call() throws Exception {
        if (ValidateUtils.isEmpty(po)) {
            return "";
        }
        try {

            loginAipService.insertLoginHistory(po);

        } catch (Exception e) {
            logger.error("记录登陆历史数据失败", e);

        }

        return null;
    }

}
