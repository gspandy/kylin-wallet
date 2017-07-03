package com.rongcapital.wallet.controller.rop;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.rongcapital.wallet.controller.rop.utils.SpringBeanUtils;


public class SpringApplicationContextAware implements ApplicationContextAware {
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringBeanUtils.setApplicationContext(applicationContext);
	}
}
