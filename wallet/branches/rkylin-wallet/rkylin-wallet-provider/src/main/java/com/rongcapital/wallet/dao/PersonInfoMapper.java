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

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rongcapital.wallet.po.PersonInfo;

/**
 * Description:
 * 
 * @author: Administrator
 * @CreateDate: 2016-8-23
 * @version: V1.0
 */
public interface PersonInfoMapper {

    int deleteByPrimaryKey(Long userId);

    int insertSelective(PersonInfo record);

    PersonInfo selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(PersonInfo record);

    int updateByPrimaryKey(PersonInfo record);
    
    PersonInfo selectByUserId(Long userId);
   
}