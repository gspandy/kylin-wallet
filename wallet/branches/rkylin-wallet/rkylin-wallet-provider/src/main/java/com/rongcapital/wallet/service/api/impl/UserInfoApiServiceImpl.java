package com.rongcapital.wallet.service.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rongcapital.wallet.api.po.UserInfoPo;
import com.rongcapital.wallet.api.po.UserLoginPo;
import com.rongcapital.wallet.api.service.UserInfoApiService;
import com.rongcapital.wallet.constant.UserInfoConstants;
import com.rongcapital.wallet.dao.PersonInfoMapper;
import com.rongcapital.wallet.dao.UserInfoMapper;
import com.rongcapital.wallet.dao.UserLoginMapper;
import com.rongcapital.wallet.po.PersonInfo;
import com.rongcapital.wallet.po.UserInfo;
import com.rongcapital.wallet.po.UserLogin;

@Service("userInfoApiService")
public class UserInfoApiServiceImpl implements UserInfoApiService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PersonInfoMapper personInfoMapper;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean register(UserInfoPo userInfo) {
        UserInfo user = new UserInfo();
        user.setUserType(userInfo.getUserType());
        user.setStatus(UserInfoConstants.ACCOUNT_OK); // 状态
        user.setUserId(userInfo.getUserId());
        user.setUserCode(userInfo.getUserCode());
        int resultId = userInfoMapper.insertSelective(user);
        if (resultId == 1) {
            Long userInfoId = user.getUserInfoId(); // 主键ID
            if (userInfo.getUserType() == 2) {
                PersonInfo person = new PersonInfo();
                person.setUserInfoId(userInfoId);
                person.setUserCname(userInfo.getUserRealName());
                person.setIdNumber(userInfo.getIdCard());
                personInfoMapper.insertSelective(person);

                UserLogin userLogin = new UserLogin();
                userLogin.setUserInfoId(userInfoId);
                userLogin.setPwdSalt(userInfo.getSalt());
                userLogin.setLoginName(userInfo.getUserName());
                userLogin.setPwdMd(userInfo.getPwd());
                userLogin.setUserType(userInfo.getUserType());
                userLoginMapper.insertSelective(userLogin);
                return true;
            }
        }
        return false;
    }

    public List<UserInfoPo> getList() throws Exception {

        return null;
    }

    public boolean login(UserInfoPo userInfo) {

        return false;
    }

    @Override
    public UserLoginPo getLoginInfo(String userName) throws Exception {

        return null;
    }

    @Override
    public UserInfoPo getUserInfoById(Long userId) throws Exception {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if (null != userInfo) {
            PersonInfo p = userInfo.getPersonInfo();

            UserInfoPo po = new UserInfoPo();
            po.setUserInfoId(userInfo.getUserInfoId());
            po.setUserId(userInfo.getUserId());
            po.setStatus(userInfo.getStatus());
            po.setUserType(userInfo.getUserType());
            po.setUserCode(userInfo.getUserCode());
            if (null != p) {
                po.setUserRealName(p.getUserCname());
                po.setIdCard(p.getIdNumber());
            }
            return po;
        }
        return null;
    }

    @Override
    public UserInfoPo getUserInfoByUserId(Long userId) throws Exception {
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (null != userInfo) {
            PersonInfo p = userInfo.getPersonInfo();

            UserInfoPo po = new UserInfoPo();
            po.setUserInfoId(userInfo.getUserInfoId());
            po.setUserId(userInfo.getUserId());
            po.setStatus(userInfo.getStatus());
            po.setUserType(userInfo.getUserType());
            po.setUserCode(userInfo.getUserCode());
            if (null != p) {
                po.setUserRealName(p.getUserCname());
                po.setIdCard(p.getIdNumber());
            }
            return po;
        }
        return null;
    }

}
