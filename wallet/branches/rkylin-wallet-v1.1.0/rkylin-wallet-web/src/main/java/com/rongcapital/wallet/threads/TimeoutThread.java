package com.rongcapital.wallet.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeoutThread  extends Thread {
 
    private static Logger logger = LoggerFactory.getLogger(TimeoutThread.class);
    /**
     * 计时器超时时间
     */
    private long timeout;
 
    /**
     * 计时是否被取消
     */
    private boolean isCanceled = false;
 
    /**
     * 构造器
     * 
     * @param timeout
     *            指定超时的时间
     */
    public TimeoutThread(long timeout,Thread currentThread) {
        super();
        this.timeout = timeout;
        // 设置本线程为守护线程
         this.setDaemon(true);
         this.setUncaughtExceptionHandler(new UncaughtExceptionHandlerImpl(currentThread));
    }
 
    /**
     * 取消计时
     */
    public synchronized void cancel() {
        isCanceled = true;
    }
 
    /**
     * 启动超时计时器
     */
    public void run() {
        try {
 
            Thread.sleep(timeout);
            if (!isCanceled){
              
            }
        } catch (Exception e) {
            logger.error("计时器启动失败", e);
        }
    }
 
    public boolean hasException() {
        return isCanceled;
    }

    /**
     * 
     *  对线程中抛出异常的处理
     *
     */
    private class UncaughtExceptionHandlerImpl implements Thread.UncaughtExceptionHandler{
 
        private Thread currentThread;
         
        public UncaughtExceptionHandlerImpl(Thread currentThread){
            this.currentThread=currentThread;
        }
         
        public void uncaughtException(Thread t, Throwable e) {
            currentThread.interrupt();
        }
         
    }
 
}

