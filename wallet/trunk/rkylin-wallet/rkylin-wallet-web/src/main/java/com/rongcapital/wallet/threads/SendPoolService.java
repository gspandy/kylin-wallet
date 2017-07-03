package com.rongcapital.wallet.threads;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rongcapital.wallet.api.po.UserLoginHistoryPo;
import com.rongcapital.wallet.api.service.LoginAipService;



public class SendPoolService {

    private static Logger logger = LoggerFactory.getLogger(SendPoolService.class);

    private ExecutorService serverPool = null;

    /**
     * 初始化线程池大小
     */
    public SendPoolService() {     
            serverPool = Executors.newSingleThreadExecutor();
      
    }

    /**
     * 启动
     * 
     * @param list
     * @param transOrderInfo
     */
    public void startServer(UserLoginHistoryPo po,LoginAipService loginAipService) {
        ServiceProcThread serverProcThd = new ServiceProcThread(po,loginAipService);
        FutureTask<String> futureTask = new FutureTask<String>(serverProcThd);//
        try {
            serverPool.submit(futureTask);
            long currentTime = System.currentTimeMillis();
            long timeOut = System.currentTimeMillis() + 2000L;

            while (currentTime < timeOut) {
                currentTime = System.currentTimeMillis();
                if (futureTask.isDone()) {// 任务都完成
                    logger.info("入库成功");
                    return;
                }
                if (!futureTask.isDone()) { // 任务没有完成
                    // logger.info("FutureTask output=" + futureTask.get());
                    // return;

                }

            }

        } catch (Exception e) {
            logger.error("入库失败", e);
         
        }
    }

    public void endService() {
        if (serverPool != null) {
            serverPool.shutdown();
        }
        System.exit(0);
    }
}
