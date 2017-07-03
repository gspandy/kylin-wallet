//package com.rongcapital.wallet.test.user.service;
//
//import java.util.List;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
//
//import com.rongcapital.wallet.pojo.UserInfo;
//import com.rongcapital.wallet.service.UserInfoService;
//import com.rongcapital.wallet.test.base.TestBase;
//
//
//
//public class UserInfoServiceTest extends TestBase {
//
//	@Autowired
//	private UserInfoService userInfoService;
//
//	
//	@Test
//	@Rollback(false)
//	public void saveTest() {
//		UserInfo user=new UserInfo();
//		user.setUserId("1001ccc");
//		user.setUserName("王五");
//		try {
//			userInfoService.save(user);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Test
//	public void getList() {
//
//		try {
//			List<UserInfo> list = userInfoService.getList();
//			if (null != list && list.size() > 0)
//				for (UserInfo user : list) {
//					System.out.println(user.getUserId());
//					System.out.println(user.getUserName());
//				}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
