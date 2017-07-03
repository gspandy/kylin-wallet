package com.rongcapital.wallet.test.base;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
/**
 * 测试基类
 * locations:加载配置文件
 * Description:
 * transactionManager:是指在spring-mybatis.xml中配置的事务管理器，名字需要对应
 * @author: Administrator
 * @CreateDate: 2016年6月16日
 * @version: V1.0
 */
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional
public class TestBase {
	
	@BeforeClass
	public static void beforeClass(){
	}
	
	@Before
	public void before(){
	}
	
	@AfterClass
	public static void afterClass(){
	}
	
	@After
	public void after(){
	}
	
	@Test
	public void test(){
	}
}
