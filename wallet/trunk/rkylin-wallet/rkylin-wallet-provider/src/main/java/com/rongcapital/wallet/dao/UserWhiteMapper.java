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

import com.rongcapital.wallet.po.UserWhite;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 * @author: Administrator
 * @CreateDate: 2016-10-11
 * @version: V1.0
 */
public interface UserWhiteMapper {
   

    /**
     * Discription:
     *
     * @param id* @return int ${return_type}
     * @author Administrator
     * @since 2016-10-11
     */
    int deleteByPrimaryKey(Long id);

    /**
     * Discription:
     *
     * @param record* @return int ${return_type}
     * @author Administrator
     * @since 2016-10-11
     */
    int insert(UserWhite record);

    /**
     * Discription:
     *
     * @param record* @return int ${return_type}
     * @author Administrator
     * @since 2016-10-11
     */
    int insertSelective(UserWhite record);

   

    /**
     * Discription:
     *
     * @param id* @return com.rongcapital.wallet.po.UserWhite ${return_type}
     * @author Administrator
     * @since 2016-10-11
     */
    UserWhite selectByPrimaryKey(Long id);

  

    /**
     * Discription:
     *
     * @param record* @return int ${return_type}
     * @author Administrator
     * @since 2016-10-11
     */
    int updateByPrimaryKeySelective(UserWhite record);

    /**
     * Discription:
     *
     * @param record* @return int ${return_type}
     * @author Administrator
     * @since 2016-10-11
     */
    int updateByPrimaryKey(UserWhite record);
    
    
    List<UserWhite> getList(UserWhite userWhite);
    
    
    int bathWhiteList(List<UserWhite> list);
}