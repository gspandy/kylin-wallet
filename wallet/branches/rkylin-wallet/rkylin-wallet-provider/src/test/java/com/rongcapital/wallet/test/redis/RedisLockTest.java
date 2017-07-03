package com.rongcapital.wallet.test.redis;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import com.rongcapital.wallet.test.base.TestBase;
import com.rongcapital.redis.sdr.lock.RedisLock;


/**
 * RedisLock测试
 * Description:
 * RedisLock是封装的一个redis分布式锁，支持单个与批量
 * @author: Administrator
 * @CreateDate: 2016年6月16日
 * @version: V1.0
 */
public class RedisLockTest extends TestBase{

	@Autowired
	private RedisLock redisLock;
	
	@Test
	public void lockTest() {
		
		try {
			String[] keys={"77","88","99","44"};
			boolean result=redisLock.tryLock(keys, 500, 2, 10, TimeUnit.SECONDS);
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
