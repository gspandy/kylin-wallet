package com.test.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.rongcapital.wallet.util.RedisIdGeneratorExt;
import com.test.base.TestBase;



public class CreateSplitFlowIdTest extends TestBase{
	@Autowired
	private RedisIdGeneratorExt redisIdGeneratorExt;
	
	@Test
	public void testCreate(){
		String id = redisIdGeneratorExt.getRadomUserId();
		System.out.println(id);
	}

}
