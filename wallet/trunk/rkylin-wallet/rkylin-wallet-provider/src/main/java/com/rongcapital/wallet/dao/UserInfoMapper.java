/**
 * <p>Title: ${file_name}</p>
 * <p>Description:${project_name} </p>
 * <p>Copyright:${date} </p>
 * <p>Company: rongshu</p>
 * <p>author: ${user}</p>
 * <p>package: ${package_name}</p>
 * @version v1.0.0
 */
package com.rongcapital.wallet.dao;

import com.rongcapital.wallet.po.UserInfo;

/**
 * Description:
 * 
 * @author: Administrator
 * @CreateDate: 2016-8-23
 * @version: V1.0
 */
public interface UserInfoMapper {

    int deleteByPrimaryKey(Long userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long userInfoId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo selectByUserId(String userId);
    
    UserInfo selectByUserIdJoin(String userId);
    
}